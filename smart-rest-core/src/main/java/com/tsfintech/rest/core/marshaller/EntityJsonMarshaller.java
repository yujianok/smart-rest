package com.tsfintech.rest.core.marshaller;

import com.tsfintech.rest.core.exception.EntityMarshalException;
import com.tsfintech.rest.core.exception.EntityUnmarshalException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

/**
 * Created by jack on 14-7-30.
 */
public class EntityJsonMarshaller implements EntityMarshaller {

    private ObjectMapper objectMapper;

    public EntityJsonMarshaller() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    }

    @Override
    public String marshal(Object entity) {
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (IOException e) {
            throw new EntityMarshalException(e);
        }
    }

    @Override
    public <T> T unmarshal(String source, Class<T> type) {
        try {
            return objectMapper.readValue(source, type);
        } catch (IOException e) {
            throw new EntityUnmarshalException(e);
        }
    }

    @Override
    public <T> List<T> unmarshalCollection(String source, Class<T> type) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
            List<T> entities = objectMapper.readValue(source, javaType);

            return entities;
        } catch (IOException e) {
            throw new EntityUnmarshalException(e);
        }
    }

    @Override
    public Object updateBySource(String source, Object entity) {

        try {
            return objectMapper.readerForUpdating(entity).readValue(source);
        } catch (IOException e) {
            throw new EntityUnmarshalException(e);
        }
    }
}
