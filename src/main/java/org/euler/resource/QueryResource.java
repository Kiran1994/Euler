package org.euler.resource;

import com.google.inject.Inject;
import org.euler.data.api.request.QueryRequest;
import org.euler.data.api.response.QueryResult;
import org.euler.service.QueryExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("query")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QueryResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryResource.class);

    private final QueryExecutionService queryExecutionService;

    @Inject
    public QueryResource(QueryExecutionService queryExecutionService) {
        this.queryExecutionService = queryExecutionService;
    }

    @POST
    public QueryResult executeQuery(QueryRequest queryRequest) {
        LOGGER.info("received request for executing query: {}", queryRequest.getQuery());

        return queryExecutionService.executeQuery(queryRequest.getQuery());
    }
}
