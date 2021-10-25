package com.sigma.dao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.error.exception.ProtocolException;
import org.springframework.stereotype.Component;

@Component
public class JSONUtils {

    private final ObjectMapper objectMapper;

    public JSONUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts object to JSON string
     *
     * @param object object to convert
     *
     * @return JSON string
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ProtocolException(e.getMessage());
        }
    }

    /**
     * Converts JSON string to object
     *
     * @param json the JSON string
     * @param type the target object type
     *
     * @param <T> generic type
     *
     * @return the object
     */
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new ProtocolException(e.getMessage());
        }
    }
}