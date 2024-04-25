package org.jjche.cat.conf;

import org.apache.ibatis.session.SqlSessionFactory;
import org.jjche.cat.integration.mybatis.CatMybatisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * mybatis 注册
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-19
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
public class MybatisAutoConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;

    @PostConstruct
    public void afterPropertiesSet() {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
            if (sqlSessionFactory != null) {
               sqlSessionFactory.getConfiguration().addInterceptor(new CatMybatisPlugin());
            }
        }
    }
}