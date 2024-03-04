package org.jjche.system.modules.mnt.util;

/**
 * <p>DataTypeEnum class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
public enum DataTypeEnum {

    /**
     * mysql
     */
    MYSQL("mysql", "mysql", "com.mysql.cj.jdbc.Driver", "`", "`", "'", "'"),

    /**
     * oracle
     */
    ORACLE("oracle", "oracle", "oracle.jdbc.driver.OracleDriver", "\"", "\"", "\"", "\""),

    /**
     * sql server
     */
    SQLSERVER("sqlserver", "sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "\"", "\"", "\"", "\""),

    /**
     * h2
     */
    H2("h2", "h2", "org.h2.Driver", "`", "`", "\"", "\""),

    /**
     * phoenix
     */
    PHOENIX("phoenix", "hbase phoenix", "org.apache.phoenix.jdbc.PhoenixDriver", "", "", "\"", "\""),

    /**
     * mongo
     */
    MONGODB("mongo", "mongodb", "mongodb.jdbc.MongoDriver", "`", "`", "\"", "\""),

    /**
     * sql4es
     */
    ELASTICSEARCH("sql4es", "elasticsearch", "nl.anchormen.sql4es.jdbc.ESDriver", "", "", "'", "'"),

    /**
     * presto
     */
    PRESTO("presto", "presto", "com.facebook.presto.jdbc.PrestoDriver", "", "", "\"", "\""),

    /**
     * moonbox
     */
    MOONBOX("moonbox", "moonbox", "moonbox.jdbc.MbDriver", "`", "`", "`", "`"),

    /**
     * cassandra
     */
    CASSANDRA("cassandra", "cassandra", "com.github.adejanovski.cassandra.jdbc.CassandraDriver", "", "", "'", "'"),

    /**
     * click house
     */
    CLICKHOUSE("clickhouse", "clickhouse", "ru.yandex.clickhouse.ClickHouseDriver", "", "", "\"", "\""),

    /**
     * kylin
     */
    KYLIN("kylin", "kylin", "org.apache.kylin.jdbc.Driver", "\"", "\"", "\"", "\""),

    /**
     * vertica
     */
    VERTICA("vertica", "vertica", "com.vertica.jdbc.Driver", "", "", "'", "'"),

    /**
     * sap
     */
    HANA("sap", "sap hana", "com.sap.db.jdbc.Driver", "", "", "'", "'"),

    /**
     * impala
     */
    IMPALA("impala", "impala", "com.cloudera.impala.jdbc41.Driver", "", "", "'", "'");

    private static final String JDBC_URL_PREFIX = "jdbc:";
    private String feature;
    private String desc;
    private String driver;
    private String keywordPrefix;
    private String keywordSuffix;
    private String aliasPrefix;
    private String aliasSuffix;

    DataTypeEnum(String feature, String desc, String driver, String keywordPrefix, String keywordSuffix, String aliasPrefix, String aliasSuffix) {
        this.feature = feature;
        this.desc = desc;
        this.driver = driver;
        this.keywordPrefix = keywordPrefix;
        this.keywordSuffix = keywordSuffix;
        this.aliasPrefix = aliasPrefix;
        this.aliasSuffix = aliasSuffix;
    }

    /**
     * <p>urlOf.</p>
     *
     * @param jdbcUrl a {@link java.lang.String} object.
     * @return a {@link DataTypeEnum} object.
     */
    public static DataTypeEnum urlOf(String jdbcUrl) {
        String url = jdbcUrl.toLowerCase().trim();
        for (DataTypeEnum dataTypeEnum : values()) {
            if (url.startsWith(JDBC_URL_PREFIX + dataTypeEnum.feature)) {
                try {
                    Class<?> aClass = Class.forName(dataTypeEnum.getDriver());
                    if (null == aClass) {
                        throw new RuntimeException("Unable to get driver instance for jdbcUrl: " + jdbcUrl);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unable to get driver instance: " + jdbcUrl);
                }
                return dataTypeEnum;
            }
        }
        return null;
    }

    /**
     * <p>Getter for the field <code>feature</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeature() {
        return feature;
    }

    /**
     * <p>Getter for the field <code>desc</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * <p>Getter for the field <code>driver</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDriver() {
        return driver;
    }

    /**
     * <p>Getter for the field <code>keywordPrefix</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKeywordPrefix() {
        return keywordPrefix;
    }

    /**
     * <p>Getter for the field <code>keywordSuffix</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKeywordSuffix() {
        return keywordSuffix;
    }

    /**
     * <p>Getter for the field <code>aliasPrefix</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAliasPrefix() {
        return aliasPrefix;
    }

    /**
     * <p>Getter for the field <code>aliasSuffix</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAliasSuffix() {
        return aliasSuffix;
    }
}
