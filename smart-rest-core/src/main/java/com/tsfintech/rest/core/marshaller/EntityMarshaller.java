package com.tsfintech.rest.core.marshaller;

import java.util.List;

/**
 * Created by jack on 14-7-30.
 */
public interface EntityMarshaller {

    public String marshal(Object entity);

    public <T> T unmarshal(String source, Class<T> type);

    public <T> List<T> unmarshalCollection(String source, Class<T> type);

    public <T> T updateBySource(String source, T entity);

}
