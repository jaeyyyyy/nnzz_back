package com.nnzz.nnzz.config.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JsonbTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonbTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        try {
            // JSONB 로 저장하기 위해 String 으로 변환하여 설정
            ps.setObject(i, objectMapper.writeValueAsString(parameter), JdbcType.OTHER.TYPE_CODE);
        } catch (IOException e) {
            throw new SQLException("Failed to convert List to JSON string", e);
        }
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return convertJsonToList(json);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertJsonToList(json);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertJsonToList(json);
    }

    private List<T> convertJsonToList(String json) throws SQLException {
        if (json != null) {
            try {
                return objectMapper.readValue(json, new TypeReference<>() {
                });
            } catch (IOException e) {
                throw new SQLException("Failed to convert JSON string to List", e);
            }
        }
        return null;
    }
}


