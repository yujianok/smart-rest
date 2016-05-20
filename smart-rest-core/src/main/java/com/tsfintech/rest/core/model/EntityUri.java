package com.tsfintech.rest.core.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by jack on 14-7-28.
 */
public class EntityUri {

    private String entityScope;

    private String entityName;

    private String entityId;

    private String operation;

    private EntityUri() {
    }

    public static EntityUri parseUri(String uri) {
        EntityUri entityUri = new EntityUri();

        String[] parts = uri.split("/");
        entityUri.setEntityScope(parts[1]);
        entityUri.setEntityName(parts[2]);

        if (parts.length >= 4) {
            try {
                String entityId = URLDecoder.decode(parts[3], "UTF-8");
                entityUri.setEntityId(entityId);
            } catch (UnsupportedEncodingException e) {
                // should not happen
            }
        }

        if (parts.length >= 5) {
            try {
                String operation = URLDecoder.decode(parts[4], "UTF-8");
                entityUri.setOperation(operation);
            } catch (UnsupportedEncodingException e) {
                // should not happen
            }
        }


        return entityUri;
    }

    public EntityUri(String entityScope, String entityName, String entityId) {
        this(entityScope, entityName, entityId, null);
    }

    public EntityUri(String entityScope, String entityName, String entityId, String operation) {
        this.entityScope = entityScope;
        this.entityName = entityName;
        this.entityId = entityId;
        this.operation = operation;
    }

    public String getEntityScope() {
        return entityScope;
    }

    public void setEntityScope(String entityScope) {
        this.entityScope = entityScope;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String toUri() {
        String uri = "/" + getEntityScope() + "/" + getEntityName();

        if (getEntityId() != null) {
            uri = uri + "/" + getEntityId();
            if (getOperation() != null) {
                uri = uri + "/" + getOperation();
            }
        }

        return uri;
    }

    public String toString() {
        return getEntityScope() +
                "." + getEntityName() +
                (getEntityId() == null ? "" : "." + getEntityId()) +
                (getOperation() == null ? "" : "." + getOperation());
    }
}
