<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageService}.mapper.${className}Mapper">

    <sql id="querySql">
        SELECT * FROM ${tableName} ${"$"}{ew.customSqlSegment}
        <#if superEntityClass = 'BaseEntityLogicDelete'>
            <choose>
            <when test="ew.customSqlSegment != null and ew.customSqlSegment != ''">
                AND is_deleted = 0
            </when>
            <otherwise>
                <where>
                    is_deleted = 0
                </where>
            </otherwise>
        </choose>
        </#if>
    </sql>

    <select id="pageQuery" resultType="${packageApi}.vo.${className}VO">
        <include refid="querySql"/>
    </select>
</mapper>
