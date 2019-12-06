package com.ws.mybatis.typehandler;

import com.ws.mybatis.model.SexEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author JunWu
 * 字符串转枚举
 */
public class SexEnumTypeHandler extends BaseTypeHandler<SexEnum> {
    /**
     * 该函数的作用:
     * 当向数据库请求的时候,将数据从 枚举 转换成 对应的属性,否则插入数据库报错
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, SexEnum sexEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, sexEnum.getSex());
    }

    @Override
    public SexEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        return SexEnum.getEnum(value);
    }

    @Override
    public SexEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        return SexEnum.getEnum(value);
    }

    @Override
    public SexEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
