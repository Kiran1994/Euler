package org.euler.data.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class QueryRequest {

    @JsonProperty("query")
    private String query;

    @JsonProperty("parameters")
    private Map<String, Object> parameters;

    public String getQuery() {
        return this.query;
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }
}
