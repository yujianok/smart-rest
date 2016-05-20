package com.tsfintech.rest.core.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tsfintech.rest.core.exception.RestServiceException;

public class EntityQuery {

    private List<QueryCondition> conditions = new ArrayList<QueryCondition>();

    private boolean paged = true;

    private int pageNo = 1;

    private int pageSize = 10;

    private String orderBy;

    private boolean asc = true;

    private boolean count = false;

    private String queryBy;

    private EntityQuery() {
    }

    public List<QueryCondition> getConditions() {
        return conditions;
    }

    public QueryCondition getCondition(String propertyName) {
        for (QueryCondition condition : conditions) {
            if (condition.getPropertyName().equals(propertyName)) {
                return condition;
            }
        }

        return null;
    }

    public String getConditionValue(String propertyName) {
        QueryCondition condition = getCondition(propertyName);

        return condition == null ? null : condition.getPropertyValue();
    }

    public boolean isPaged() {
        return paged;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public boolean isCount() {
        return count;
    }

    public String getQueryBy() {
        return queryBy;
    }

    public static EntityQuery parseQuery(String queryString) {
        EntityQuery query = new EntityQuery();
        if (StringUtils.isEmpty(queryString)) {
            return query;
        }

        String[] queryParts = queryString.split("&");
        for (String queryPart : queryParts) {
            String[] conditionParts = queryPart.split("=");
            String propertyName = conditionParts[0].trim();
            String propertyValue = conditionParts[1].trim();

            QueryComparator comparator = QueryComparator.EQUAL;
            for (QueryComparator queryComparator : QueryComparator.values()) {
                if (propertyValue.startsWith(queryComparator.name() + ":")) {
                    comparator = queryComparator;
                    propertyValue = propertyValue.replace(queryComparator.name() + ":", "");
                    break;
                }
            }

            if ("_paged".equals(propertyName)) {
                query.paged = Boolean.valueOf(propertyValue);
            } else if ("_pageSize".equals(propertyName)) {
                query.pageSize = Integer.parseInt(propertyValue);
                if (query.pageSize > 1000) {
                    throw new RestServiceException("page size is to large, the largest page size is 1000.");
                }
            } else if ("_pageNo".equals(propertyName)) {
                query.pageNo = Integer.parseInt(propertyValue);
            } else if ("_orderBy".equals(propertyName)) {
                query.orderBy = propertyValue;
            } else if ("_asc".equals(propertyName)) {
                query.asc = Boolean.valueOf(propertyValue);
            } else if ("_count".equals(propertyName)) {
                query.count = Boolean.valueOf(propertyValue);
            }  else if ("_queryBy".equals(propertyName)) {
                query.queryBy = propertyValue;
            } else {
                query.addCondition(propertyName, propertyValue, comparator);
            }
        }

        return query;
    }

    public static EntityQuery buildQuery() {
        return new EntityQuery();
    }

    public EntityQuery addCondition(String propertyName, String propertyValue) {
        this.conditions.add(new QueryCondition(propertyName, propertyValue, QueryComparator.EQUAL));
        return this;
    }

    public EntityQuery addCondition(String propertyName, String propertyValue, QueryComparator comparator) {
        this.conditions.add(new QueryCondition(propertyName, propertyValue, comparator));
        return this;
    }

    public EntityQuery orderBy(String propertyName, boolean asc) {
        this.orderBy = propertyName;
        this.asc = asc;
        return this;
    }

    public EntityQuery pageBy(int pageNo, int pageSize) {
        this.paged = true;
        this.pageNo = pageNo;
        this.pageSize = pageSize;

        return this;
    }

    public EntityQuery disablePage() {
        this.paged = false;

        return this;
    }

    public EntityQuery forCount() {
        this.paged = false;
        this.count = true;

        return this;
    }

    public EntityQuery queryBy(String queryBy) {
        this.queryBy = queryBy;

        return this;
    }

    public String buildQueryString() {
        StringBuffer buffer = new StringBuffer();
        for (QueryCondition condition : conditions) {
            buffer.append(condition.getPropertyName())
                    .append("=")
                    .append(condition.getComparator().name())
                    .append(":")
                    .append(condition.getPropertyValue())
                    .append("&");
        }

        if (isPaged()) {
            buffer.append("_paged")
                    .append("=")
                    .append(isPaged())
                    .append("&")
                    .append("_pageNo")
                    .append("=")
                    .append(getPageNo())
                    .append("&")
                    .append("_pageSize")
                    .append("=")
                    .append(getPageSize())
                    .append("&");
        } else {
            buffer.append("_paged")
                    .append("=")
                    .append(isPaged())
                    .append("&");
        }

        if (StringUtils.isNotEmpty(getOrderBy())) {
            buffer.append("_orderBy")
                    .append("=")
                    .append(getOrderBy())
                    .append("&")
                    .append("_asc")
                    .append("=")
                    .append(isAsc())
                    .append("&");
        }

        if (isCount()) {
            buffer.append("_count")
                    .append("=")
                    .append(isCount())
                    .append("&");
        }

        if (StringUtils.isNotEmpty(getQueryBy())) {
            buffer.append("_queryBy")
                    .append("=")
                    .append(getQueryBy())
                    .append("&");
        }

        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

}
