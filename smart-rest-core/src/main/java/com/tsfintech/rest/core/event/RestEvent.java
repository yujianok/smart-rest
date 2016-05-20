package com.tsfintech.rest.core.event;

import com.tsfintech.rest.core.model.EntityUri;

/**
 * Created by jack on 15-1-12.
 */
public class RestEvent {

    private String entityScope;

    private String entityType;

    private String entityId;

    private String operation;

    private RestEventType eventType;

    private Object target;

    public RestEvent(EntityUri entityUri, RestEventType eventType, Object target) {
        this.entityScope = entityUri.getEntityScope();
        this.entityType = entityUri.getEntityName();
        this.entityId = entityUri.getEntityId();
        this.operation = entityUri.getOperation();
        this.eventType = eventType;
        this.target = target;
    }

    public String getEntityScope() {
        return entityScope;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public RestEventType getEventType() {
        return eventType;
    }

    public Object getTarget() {
        return target;
    }

    public String getOperation() {
        return operation;
    }
}
