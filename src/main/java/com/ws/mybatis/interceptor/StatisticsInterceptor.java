package com.ws.mybatis.interceptor;

import com.ws.mybatis.util.MappedStatementUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * @author JunWu
 * 只拦截查询方法
 * 查询统计数量拦截器CacheKey var5, BoundSql var6
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Slf4j
public class StatisticsInterceptor implements Interceptor {

    private boolean enable = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        Executor executor = (Executor) invocation.getTarget();

        // 创建统计数量的MapperStatement
        MappedStatement countMs = MappedStatementUtils.crateMappedStatement(mappedStatement, "_COUNT");
        // 创建统计SQL
        String countSql = MappedStatementUtils.createCountSql(boundSql.getSql());
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        // 查询
        List<Object> query = executor.query(countMs, boundSql.getParameterObject(), RowBounds.DEFAULT, null, null, countBoundSql);
        Long count = (Long) query.get(0);
        log.info("SQL -> {} , 参数 -> {} , 查询结果大约数 -> {}", new Object[]{boundSql.getSql(), boundSql.getParameterObject(), count});

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
