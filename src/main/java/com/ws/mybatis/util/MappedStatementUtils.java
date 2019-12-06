package com.ws.mybatis.util;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author JunWu
 * 工具类
 */
public class MappedStatementUtils {

    public static final String PREFIX = "select count(*) ";
    public static final String FROM_STR = "from";

    private MappedStatementUtils() {
    }

    /**
     * 生成统计数量SQL
     *
     * @param querySql
     * @return
     */
    public static String createCountSql(String querySql) {
        StringBuilder sqlBuilder = new StringBuilder(PREFIX);
        int fromIndex = querySql.indexOf(FROM_STR);
        StringBuilder append = sqlBuilder.append(querySql.substring(fromIndex));
        return append.toString();
    }

    /**
     * 根据原ms生成新的ms
     *
     * @param ms
     * @param postfix
     * @return
     */
    public static MappedStatement crateMappedStatement(MappedStatement ms, String postfix) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), postfix, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            String[] var4 = ms.getKeyProperties();
            int var5 = var4.length;
            for (int var6 = 0; var6 < var5; ++var6) {
                String keyProperty = var4[var6];
                keyProperties.append(keyProperty).append(",");
            }

            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        List<ResultMap> resultMaps = new ArrayList();
        ResultMap resultMap = (new org.apache.ibatis.mapping.ResultMap.Builder(ms.getConfiguration(), ms.getId(), Long.class, Collections.EMPTY_LIST)).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
}
