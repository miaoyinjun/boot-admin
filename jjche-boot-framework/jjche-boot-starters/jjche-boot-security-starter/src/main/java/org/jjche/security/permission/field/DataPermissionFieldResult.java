package org.jjche.security.permission.field;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jjche.common.constant.EnumConstant;
import org.jjche.common.permission.DataPermissionFieldFilterable;
import org.jjche.common.permission.DataPermissionFieldMetaSetter;
import org.jjche.common.util.ClassUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.common.vo.DataPermissionFieldResultVO;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * 支持过滤的结构，用于在AOP方法中对要显示的数据进行控制
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-11
 */
public class DataPermissionFieldResult<T> implements DataPermissionFieldFilterable<T>, DataPermissionFieldMetaSetter {

    @Getter
    @Setter
    @ApiModelProperty("数据")
    private Iterable<T> records;
    @ApiModelProperty("字段")
    private List<DataPermissionFieldResultVO> meta;

    /**
     * <p>
     * 过滤集合
     * </p>
     *
     * @param rows 集合
     * @param <T>  a T object.
     * @return 结果
     */
    public static <T> DataPermissionFieldResult<T> build(Iterable<T> rows) {
        DataPermissionFieldResult<T> result = new DataPermissionFieldResult<>();
        result.setRecords(rows);
        return result;
    }

    /**
     * <p>
     * 过滤单个
     * </p>
     *
     * @param row 数据
     * @param <T> a T object.
     * @return /
     */
    public static <T> DataPermissionFieldResult<T> build(T row) {
        return build(CollUtil.newArrayList(row));
    }

    /**
     * <p>
     * 转换Excel所需格式
     * </p>
     *
     * @param dataPermissionFieldResult 内容
     * @return /
     */
    public static List<Map<String, Object>> toExcelListMap(DataPermissionFieldResult dataPermissionFieldResult) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (dataPermissionFieldResult != null) {
            Iterable dataList = dataPermissionFieldResult.getData();
            List<DataPermissionFieldResultVO> meta = dataPermissionFieldResult.getMeta();

            if (CollUtil.isNotEmpty(dataList)) {
                List<Field> fieldList = ClassUtil.getAllFields(CollUtil.getFirst(dataList).getClass());
                for (Object o : dataList) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (DataPermissionFieldResultVO fieldResultVO : meta) {
                        Boolean isAccessible = fieldResultVO.getIsAccessible();
                        if (isAccessible) {
                            String name = fieldResultVO.getName();
                            String code = fieldResultVO.getCode();
                            for (Field field : fieldList) {
                                if (StrUtil.equals(field.getName(), code)) {
                                    String value = new JSONObject(o).get(code, String.class);
                                    //处理枚举
                                    if (field.getType().isEnum()) {
                                        Class<? extends Enum<?>> clazz = (Class<? extends Enum<?>>) field.getType();
                                        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(clazz, EnumConstant.DESC);
                                        value = enumMap.get(value).toString();
                                    }//格式化时间
                                    else if (field.getType().isAssignableFrom(Timestamp.class)) {
                                        value = DateUtil.format(DateUtil.parse(value), DatePattern.NORM_DATETIME_PATTERN);
                                    }
                                    map.put(name, value);
                                    break;
                                }
                            }
                        }
                    }
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(Function<T, T> filterFunc) {
        for (T row : records) {
            filterFunc.apply(row);
        }
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Iterable<T> getData() {
        return this.records;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataPermissionFieldResultVO> getMeta() {
        return this.meta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMeta(List<DataPermissionFieldResultVO> dataResources) {
        this.meta = dataResources;
    }
}
