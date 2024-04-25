//package org.jjche.core.convert;
//
//import ma.glasnost.orika.Converter;
//import ma.glasnost.orika.Mapper;
//import ma.glasnost.orika.MapperFactory;
//import ma.glasnost.orika.MappingContext;
//import ma.glasnost.orika.converter.BidirectionalConverter;
//import ma.glasnost.orika.converter.ConverterFactory;
//import ma.glasnost.orika.impl.ConfigurableMapper;
//import ma.glasnost.orika.impl.DefaultMapperFactory;
//import ma.glasnost.orika.metadata.ClassMapBuilder;
//import ma.glasnost.orika.metadata.Type;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import java.util.Map;
//
///**
// * <p>
// * 转换类实现
// * </p>
// *
// * @author miaoyj
// * @version 1.0.0-SNAPSHOT
// * @since 2020-07-09
// */
//public class OriKaMapper extends ConfigurableMapper implements ApplicationContextAware {
//
//    private MapperFactory factory;
//    private ApplicationContext applicationContext;
//
//    /**
//     * <p>Constructor for OriKaMapper</p>
//     */
//    public OriKaMapper() {
//        super(false);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void configure(final MapperFactory factory) {
//        //全局注册Boolean与Integer转换器
//        factory.getConverterFactory().registerConverter(new BoolIntegerConverter());
//        this.factory = factory;
//        addAllSpringBeans(applicationContext);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void configureFactoryBuilder(final DefaultMapperFactory.Builder factoryBuilder) {
//        factoryBuilder.mapNulls(false);
//    }
//
//    /**
//     * Constructs and registers a {@link ClassMapBuilder} into the {@link MapperFactory} using a {@link Mapper}.
//     *
//     * @param mapper
//     */
//    private void addMapper(final Mapper<?, ?> mapper) {
//        factory.classMap(mapper.getAType(),
//                        mapper.getBType())
//                .byDefault()
//                .customize((Mapper) mapper)
//                .mapNulls(false)
//                .mapNullsInReverse(false)
//                .register();
//    }
//
//    /**
//     * Registers a {@link Converter} into the {@link ConverterFactory}.
//     *
//     * @param converter
//     */
//    private void addConverter(final Converter<?, ?> converter) {
//        factory.getConverterFactory().registerConverter(converter);
//    }
//
//    /**
//     * Scans the appliaction context and registers all Mappers and Converters found in it.
//     *
//     * @param applicationContext
//     */
//    private void addAllSpringBeans(final ApplicationContext applicationContext) {
//        final Map<String, Mapper> mappers = applicationContext.getBeansOfType(Mapper.class);
//        for (final Mapper mapper : mappers.values()) {
//            addMapper(mapper);
//        }
//        final Map<String, Converter> converters = applicationContext.getBeansOfType(Converter.class);
//        for (final Converter converter : converters.values()) {
//            addConverter(converter);
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//        init();
//    }
//
//    protected class BoolIntegerConverter extends BidirectionalConverter<Boolean, Integer> {
//        @Override
//        public Integer convertTo(Boolean aBoolean, Type<Integer> type, MappingContext mappingContext) {
//            return aBoolean ? 1 : 0;
//        }
//
//        @Override
//        public Boolean convertFrom(Integer integer, Type<Boolean> type, MappingContext mappingContext) {
//            return integer == 1;
//        }
//    }
//
//}
