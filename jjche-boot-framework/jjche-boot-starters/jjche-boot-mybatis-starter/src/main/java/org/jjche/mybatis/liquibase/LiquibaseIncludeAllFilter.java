package org.jjche.mybatis.liquibase;

import cn.hutool.core.text.AntPathMatcher;
import liquibase.changelog.IncludeAllFilter;

public class LiquibaseIncludeAllFilter implements IncludeAllFilter {
    @Override
    public boolean include(String changeLogPath) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match("liquibase/*.xml", changeLogPath);
    }
}
