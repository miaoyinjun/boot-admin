package org.jjche.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.jjche.mybatis.extension.MySqlInjector;
import org.jjche.mybatis.handler.MyMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Configuration
@Import({MyMetaObjectHandler.class, MySqlInjector.class})
public class MybatisAutoConfiguration {
    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     *
     * @return a {@link com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor} object.
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /**分页插件*/
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        /**设置最大单页限制数量，默认 500 条*/
        paginationInnerInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        /**防止全表更新与删除插件*/
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        /**数据权限插件*/
//        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
//        MyDataPermissionHandler myDataPermissionHandler = new MyDataPermissionHandler();
//        dataPermissionInterceptor.setDataPermissionHandler(myDataPermissionHandler);
//        interceptor.addInnerInterceptor(dataPermissionInterceptor);
        return interceptor;
    }
//
//    /**
//     * <p>排序</p>
//     *
//     * @return a {@link tk.mybatis.orderbyhelper.OrderByHelper} object.
//     */
//    @Bean
//    public OrderByHelper orderByHelper() {
//        return new OrderByHelper();
//    }

//    /**
//     * 数据权限插件
//     *
//     * @return DataScopeInterceptor
//     */
//    @Bean
//    public DataScopeInterceptor dataScopeInterceptor() {
//        return new DataScopeInterceptor();
//    }

}
