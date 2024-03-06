package org.jjche.swagger.conf;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.members.ResolvedField;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import org.jjche.common.annotation.IgnoreField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.common.Compatibility;
import springfox.documentation.schema.Maps;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.Types;
import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeField;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterMetadataAccessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Predicates.*;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.Types.typeNameFor;

/**
 * <p>
 * 假如接收参数的实体类中关联了其他对象，那么swagger2的页面中参数应该会多出来这些，
 * dept.id，dept.deptName，或者集合属性，roles[0].id，roles[0].roleName等等。
 * 这些属性有可能是不需要用来接收参数的，出现在文档中会给前端开发人员带来困惑
 *
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-09-14
 */
@Component
@Primary
public class CustomizeModelAttributeParameterExpander extends ModelAttributeParameterExpander {
    private static final Logger LOG = LoggerFactory.getLogger(ModelAttributeParameterExpander.class);
    private final FieldProvider fields;
    private final AccessorsProvider accessors;
    private final EnumTypeDeterminer enumTypeDeterminer;

    @Autowired
    protected DocumentationPluginsManager pluginsManager;

    @Autowired
    /**
     * <p>Constructor for CustomizeModelAttributeParameterExpander.</p>
     *
     * @param fields a {@link springfox.documentation.schema.property.field.FieldProvider} object.
     * @param accessors a {@link springfox.documentation.schema.property.bean.AccessorsProvider} object.
     * @param enumTypeDeterminer a {@link springfox.documentation.spi.schema.EnumTypeDeterminer} object.
     */
    public CustomizeModelAttributeParameterExpander(FieldProvider fields, AccessorsProvider accessors, EnumTypeDeterminer enumTypeDeterminer) {
        super(fields, accessors, enumTypeDeterminer);
        this.fields = fields;
        this.accessors = accessors;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }

    /**
     * <p>determineScalarParameterType.</p>
     *
     * @param consumes a {@link java.util.Set} object.
     * @param method   a {@link org.springframework.http.HttpMethod} object.
     * @return a {@link java.lang.String} object.
     */
    public static String determineScalarParameterType(Set<? extends MediaType> consumes, HttpMethod method) {
        String parameterType = "query";

        if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED)
                && method == HttpMethod.POST) {
            parameterType = "form";
        } else if (consumes.contains(MediaType.MULTIPART_FORM_DATA)
                && method == HttpMethod.POST) {
            parameterType = "formData";
        }

        return parameterType;
    }


    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Compatibility<Parameter, RequestParameter>> expand(ExpansionContext context) {
        List<Compatibility<Parameter, RequestParameter>> parameters = new ArrayList();
        Set<PropertyDescriptor> propertyDescriptors = propertyDescriptors(context.getParamType().getErasedType());
        Map<Method, PropertyDescriptor> propertyLookupByGetter
                = propertyDescriptorsByMethod(context.getParamType().getErasedType(), propertyDescriptors);
        Iterable<ResolvedMethod> getters = FluentIterable.from(accessors.in(context.getParamType()))
                .filter(onlyValidGetters(propertyLookupByGetter.keySet()));

        Map<String, ResolvedField> fieldsByName = FluentIterable.from(this.fields.in(context.getParamType()))
                .uniqueIndex(new Function<ResolvedField, String>() {
                    @Override
                    public String apply(ResolvedField input) {
                        return input.getName();
                    }
                });


        LOG.debug("Expanding parameter type: {}", context.getParamType());
        final AlternateTypeProvider alternateTypeProvider = context.getDocumentationContext().getAlternateTypeProvider();

        FluentIterable<ModelAttributeField> attributes =
                allModelAttributes(
                        propertyLookupByGetter,
                        getters,
                        fieldsByName,
                        alternateTypeProvider);

        FluentIterable<ModelAttributeField> expendables = attributes
                .filter(not(simpleType()))
                .filter(not(recursiveType(context)));
        for (ModelAttributeField each : expendables) {
            LOG.debug("Attempting to expand expandable property: {}", each.getName());
            parameters.addAll(
                    expand(
                            context.childContext(
                                    nestedParentName(context.getParentName(), each),
                                    each.getFieldType(),
                                    context.getOperationContext())));
        }

        FluentIterable<ModelAttributeField> collectionTypes = attributes
                .filter(and(isCollection(), not(recursiveCollectionItemType(context.getParamType()))));
        for (ModelAttributeField each : collectionTypes) {
            LOG.debug("Attempting to expand collection/array field: {}", each.getName());

            ResolvedType itemType = collectionElementType(each.getFieldType());
            if (Types.isBaseType(itemType) || enumTypeDeterminer.isEnum(itemType.getErasedType())) {
                parameters.add(simpleFields(context.getParentName(), context, each));
            } else {
                ExpansionContext childContext = context.childContext(
                        nestedParentName(context.getParentName(), each),
                        itemType,
                        context.getOperationContext());
                if (!context.hasSeenType(itemType)) {
                    parameters.addAll(expand(childContext));
                }
            }
        }

        FluentIterable<ModelAttributeField> simpleFields = attributes.filter(simpleType());
        for (ModelAttributeField each : simpleFields) {
            parameters.add(simpleFields(context.getParentName(), context, each));
        }
        return parameters.stream().filter(this.hiddenParameter().negate()).filter(this.voidParameters().negate()).collect(Collectors.toList());
    }

    private FluentIterable<ModelAttributeField> allModelAttributes(
            Map<Method, PropertyDescriptor> propertyLookupByGetter,
            Iterable<ResolvedMethod> getters,
            Map<String, ResolvedField> fieldsByName,
            AlternateTypeProvider alternateTypeProvider) {

        FluentIterable<ModelAttributeField> modelAttributesFromGetters = from(getters)
                .transform(toModelAttributeField(fieldsByName, propertyLookupByGetter, alternateTypeProvider));

        FluentIterable<ModelAttributeField> modelAttributesFromFields = from(fieldsByName.values())
                .filter(publicFields())
                .transform(toModelAttributeField(alternateTypeProvider));

        return FluentIterable.from(Sets.union(
                modelAttributesFromFields.toSet(),
                modelAttributesFromGetters.toSet()));
    }

    private Function<ResolvedField, ModelAttributeField> toModelAttributeField(
            final AlternateTypeProvider alternateTypeProvider) {

        return new Function<ResolvedField, ModelAttributeField>() {
            @Override
            public ModelAttributeField apply(ResolvedField input) {
                return new ModelAttributeField(
                        alternateTypeProvider.alternateFor(input.getType()),
                        input.getName(),
                        input,
                        input);
            }
        };
    }

    private Predicate<ResolvedField> publicFields() {
        return new Predicate<ResolvedField>() {
            @Override
            public boolean apply(ResolvedField input) {
                return input.isPublic();
            }
        };
    }

    private java.util.function.Predicate<Compatibility<Parameter, RequestParameter>> voidParameters() {
        return (input) -> {
            return ResolvedTypes.isVoid((ResolvedType) input.getLegacy().flatMap(Parameter::getType).orElse(null));
        };
    }

    private Predicate<ModelAttributeField> recursiveCollectionItemType(final ResolvedType paramType) {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return equal(collectionElementType(input.getFieldType()), paramType);
            }
        };
    }

    private java.util.function.Predicate<Compatibility<Parameter, RequestParameter>> hiddenParameter() {
        return (c) -> {
            return (Boolean) c.getLegacy().map(Parameter::isHidden).orElse(false);
        };
    }

    private Compatibility<Parameter, RequestParameter> simpleFields(
            String parentName,
            ExpansionContext context,
            ModelAttributeField each) {
        LOG.debug("Attempting to expand field: {}", each);
        String dataTypeName = Optional.fromNullable(typeNameFor(each.getFieldType().getErasedType()))
                .or(each.getFieldType().getErasedType().getSimpleName());
        LOG.debug("Building parameter for field: {}, with type: ", each, each.getFieldType());
        ParameterExpansionContext parameterExpansionContext = new
                ParameterExpansionContext(dataTypeName, parentName,
                determineScalarParameterType(
                        context.getOperationContext().consumes(),
                        context.getOperationContext().httpMethod()),
                new ModelAttributeParameterMetadataAccessor(each.annotatedElements(),
                        each.getFieldType(), each.getName()), context.getDocumentationType(),
                new ParameterBuilder(), new RequestParameterBuilder());
        return pluginsManager.expandParameter(parameterExpansionContext);
    }

    private Predicate<ModelAttributeField> recursiveType(final ExpansionContext context) {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return context.hasSeenType(input.getFieldType());
            }
        };
    }

    private Predicate<ModelAttributeField> simpleType() {
        return and(not(isCollection()), not(isMap()),
                or(
                        belongsToJavaPackage(),
                        isBaseType(),
                        isEnum()));
    }

    private Predicate<ModelAttributeField> isCollection() {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return isContainerType(input.getFieldType());
            }
        };
    }

    private Predicate<ModelAttributeField> isMap() {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return Maps.isMapType(input.getFieldType());
            }
        };
    }

    private Predicate<ModelAttributeField> isEnum() {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return enumTypeDeterminer.isEnum(input.getFieldType().getErasedType());
            }
        };
    }

    private Predicate<ModelAttributeField> belongsToJavaPackage() {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return ClassUtils.getPackageName(input.getFieldType().getErasedType()).startsWith("java.lang");
            }
        };
    }

    private Predicate<ModelAttributeField> isBaseType() {
        return new Predicate<ModelAttributeField>() {
            @Override
            public boolean apply(ModelAttributeField input) {
                return Types.isBaseType(input.getFieldType())
                        || input.getFieldType().isPrimitive();
            }
        };
    }

    private Function<ResolvedMethod, ModelAttributeField> toModelAttributeField(
            final Map<String, ResolvedField> fieldsByName,
            final Map<Method, PropertyDescriptor> propertyLookupByGetter,
            final AlternateTypeProvider alternateTypeProvider) {
        return new Function<ResolvedMethod, ModelAttributeField>() {
            @Override
            public ModelAttributeField apply(ResolvedMethod input) {
                String name = propertyLookupByGetter.get(input.getRawMember()).getName();
                return new ModelAttributeField(
                        fieldType(alternateTypeProvider, input),
                        name,
                        input,
                        fieldsByName.get(name));
            }
        };
    }

    private Predicate<ResolvedMethod> onlyValidGetters(final Set<Method> methods) {
        return new Predicate<ResolvedMethod>() {
            @Override
            public boolean apply(ResolvedMethod input) {
                return methods.contains(input.getRawMember());
            }
        };
    }

    private String nestedParentName(String parentName, ModelAttributeField attribute) {
        String name = attribute.getName();
        ResolvedType fieldType = attribute.getFieldType();
        if (isContainerType(fieldType) && !Types.isBaseType(collectionElementType(fieldType))) {
            name += "[0]";
        }

        if (isNullOrEmpty(parentName)) {
            return name;
        }
        return String.format("%s.%s", parentName, name);
    }

    private ResolvedType fieldType(AlternateTypeProvider alternateTypeProvider, ResolvedMethod method) {
        return alternateTypeProvider.alternateFor(method.getType());
    }

    private Set<PropertyDescriptor> propertyDescriptors(final Class<?> clazz) {
        try {
            Set<PropertyDescriptor> beanProps = new HashSet<>();
            PropertyDescriptor[] descriptors = getBeanInfo(clazz).getPropertyDescriptors();

            for (PropertyDescriptor descriptor : descriptors) {
                Field field = null;
                try {
                    field = ReflectUtil.getField(clazz, descriptor.getName());
                } catch (Exception e) {
                    LOG.debug(String.format("Failed to get bean properties on (%s)", clazz), e);
                }
                if (field != null) {
                    field.setAccessible(true);
                    IgnoreField ignoreField = field.getDeclaredAnnotation(IgnoreField.class);
                    if (ignoreField != null) {
                        continue;
                    }
                }

                if (descriptor.getReadMethod() != null) {
                    beanProps.add(descriptor);
                }
            }
            return beanProps;
        } catch (Exception e) {
            LOG.warn(String.format("Failed to get bean properties on (%s)", clazz), e);
        }
        return newHashSet();
    }

    private Map<Method, PropertyDescriptor> propertyDescriptorsByMethod(
            final Class<?> clazz,
            Set<PropertyDescriptor> propertyDescriptors) {
        return FluentIterable.from(propertyDescriptors)
                .filter(new Predicate<PropertyDescriptor>() {
                    @Override
                    public boolean apply(PropertyDescriptor input) {
                        return input.getReadMethod() != null
                                && !clazz.isAssignableFrom(Collection.class)
                                && !"isEmpty".equals(input.getReadMethod().getName());
                    }
                })
                .uniqueIndex(new Function<PropertyDescriptor, Method>() {
                    @Override
                    public Method apply(PropertyDescriptor input) {
                        return input.getReadMethod();
                    }
                });

    }

    @VisibleForTesting
    BeanInfo getBeanInfo(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz);
    }
}
