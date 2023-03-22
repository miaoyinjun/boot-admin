package org.jjche.system.modules.system.conf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import lombok.RequiredArgsConstructor;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.constant.PackageConstant;
import org.jjche.system.modules.system.api.dto.DictDetailDTO;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 枚举初始化到字典
 * </p>
 *
 * @author miaoyj
 * @version 1.0.4-SNAPSHOT
 * @since 2021-12-03
 */
@Component
@RequiredArgsConstructor
public class DictRunner implements ApplicationRunner {

    /**
     * Constant <code>DICT_ENUMS</code>
     */
    public final static Map<String, List<DictDetailDTO>> DICT_ENUMS = MapUtil.newHashMap();

    /**
     * {@inheritDoc}
     * <p>
     * 项目启动时 枚举初始化到字典
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        StaticLog.info("--------------------枚举初始化到字典---------------------");
        Set<Class<?>> enums = ClassUtil.scanPackage(PackageConstant.BASE_PATH);
        if (CollUtil.isNotEmpty(enums)) {
            Predicate<Class> condition = (p) -> Enum.class.isAssignableFrom(p);
            enums = enums.stream().filter(condition).collect(Collectors.toSet());
            for (Class<?> en : enums) {
                Object[] enumConstants = en.getEnumConstants();
                String enumName = en.getSimpleName();
                List<DictDetailDTO> displayValues =
                        Arrays.stream(enumConstants)
                                .filter(Objects::nonNull)
                                .map(item -> {
                                    Class<?> currentClass = item.getClass();
                                    Field indexField = ReflectionUtils.findField(currentClass, EnumConstant.VALUE);
                                    Field descField = ReflectionUtils.findField(currentClass, EnumConstant.DESC);
                                    if (ObjectUtil.isNotNull(indexField) && ObjectUtil.isNotNull(descField)) {
                                        ReflectionUtils.makeAccessible(indexField);
                                        Object value = ReflectionUtils.getField(indexField, item);

                                        ReflectionUtils.makeAccessible(descField);
                                        Object desc = ReflectionUtils.getField(descField, item);
                                        DictDetailDTO dictDO = new DictDetailDTO();
                                        dictDO.setLabel(String.valueOf(desc));
                                        dictDO.setValue(String.valueOf(value));
                                        return dictDO;
                                    } else {
                                        return null;
                                    }
                                }).collect(Collectors.toList());
                displayValues = CollUtil.removeNull(displayValues);
                //不保存存相同的key
                if (!DICT_ENUMS.containsKey(enumName) && CollUtil.isNotEmpty(displayValues)) {
                    DICT_ENUMS.put(enumName, displayValues);
                }
            }
        }
        StaticLog.info("--------------------枚举初始化到字典完成---------------------");
    }
}
