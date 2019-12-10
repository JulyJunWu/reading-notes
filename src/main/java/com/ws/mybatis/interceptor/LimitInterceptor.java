package com.ws.mybatis.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author JunWu
 * 查询限制条数
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class LimitInterceptor implements Interceptor {

    private boolean enable;

    private static final String TABLE_NAME = "$_$_TEMP_NAME_$_$";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        // h 是代理类中的变量 InvocationHandler h;
        while (metaObject.hasGetter("h")) {
            statementHandler = (StatementHandler) metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(statementHandler);
        }

        SqlCommandType commandType = (SqlCommandType) metaObject.getValue("delegate.mappedStatement.sqlCommandType");
        if (SqlCommandType.SELECT == commandType) {
            String sql = (String) metaObject.getValue("delegate.boundSql.sql");
            String newSql = "select * from (" + sql + ") " + TABLE_NAME + " LIMIT 10";
            metaObject.setValue("delegate.boundSql.sql", newSql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return enable ? Plugin.wrap(o, this) : o;
    }

    @Override
    public void setProperties(Properties properties) {
        Boolean enable = Boolean.valueOf(properties.getProperty("enable"));
        if (enable != null) {
            this.enable = enable;
        }
    }
}
