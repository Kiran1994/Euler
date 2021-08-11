package org.euler.data.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import graphql.GraphQLError;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryResult {

    public QueryResult() {}

    @JsonProperty("data")
    private Map<String, Object> data;

    @JsonProperty("errors")
    private List<GraphQLError> errors;

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setErrors(List<GraphQLError> errors) {
        this.errors = errors;
    }
}
