package org.euler.service;

import com.google.inject.Inject;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.euler.data.api.response.QueryResult;
import org.euler.data.graphql.handler.UserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class QueryExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryExecutionService.class);

    private final GraphQL graphQL;
    private final UserHandler userHandler;

    @Inject
    public QueryExecutionService(GraphQL graphQL, UserHandler userHandler) {
        this.graphQL = graphQL;
        this.userHandler = userHandler;
    }

    public QueryResult executeQuery(String query) {
        DataLoaderRegistry registry = new DataLoaderRegistry();

        for (Map.Entry<String, DataLoader> e : userHandler.getDataLoaderMap().entrySet()) {
            registry.register(e.getKey(), e.getValue());
        }

        ExecutionResult result = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(query)
                .dataLoaderRegistry(registry)
                .build()
        );

        QueryResult queryResult = new QueryResult();
        if (result.getErrors().isEmpty()) {
            queryResult.setData(result.getData());
        } else {
            LOGGER.error("error occurred while executing query: {}", result.getErrors());
            queryResult.setErrors(result.getErrors());
        }

        return queryResult;
    }
}
