package org.jjche.mybatis.extension.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 根据自定义条件，逻辑批量删除并自动填充
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-26
 */
public class LogicDeleteBatchWithFill extends AbstractMethod {
    /**
     * mapper 对应的方法名
     */
    private static final String MAPPER_METHOD = "deleteBatchWithFill";

    /**
     * {@inheritDoc}
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql;
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_BY_MAP;
        if (tableInfo.isWithLogicDelete()) {
            List<TableFieldInfo> fieldInfos = tableInfo.getFieldList().stream()
                    .filter(i -> i.getFieldFill() == FieldFill.UPDATE || i.getFieldFill() == FieldFill.INSERT_UPDATE)
                    .collect(toList());
            if (CollectionUtils.isNotEmpty(fieldInfos)) {
                String sqlSet = "SET " + fieldInfos.stream().map(i -> i.getSqlSet(ENTITY_DOT)).collect(joining(EMPTY))
                        + tableInfo.getLogicDeleteSql(false, false);
                sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlSet,
                        sqlWhereEntityWrapper(true, tableInfo));
            } else {
                sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlLogicSet(tableInfo),
                        sqlWhereEntityWrapper(true, tableInfo));
            }
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return this.addUpdateMappedStatement(mapperClass, modelClass, MAPPER_METHOD, sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE_BY_MAP;
            sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo));
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return this.addDeleteMappedStatement(mapperClass, MAPPER_METHOD, sqlSource);
        }
    }
}
