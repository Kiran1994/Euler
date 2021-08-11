package org.euler.data.graphql;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import org.euler.data.graphql.handler.DataHandler;
import org.euler.utils.Constants;

import java.io.File;
import java.util.List;
import java.util.Map;

public class EulerGraphQLSchema {

    private GraphQLSchema schema;

    @Inject
    public EulerGraphQLSchema(@Named(Constants.Guice.DATA_HANDLERS) List<DataHandler> dataHandlerList) {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        File schemaFile = new File("target/classes/schema.graphqls");

        TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring().type(buildWiring(dataHandlerList)).build();

        this.schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    }

    private TypeRuntimeWiring buildWiring(List<DataHandler> dataHandlerList) {
        TypeRuntimeWiring.Builder builder = TypeRuntimeWiring.newTypeWiring(SchemaConstants.Types.QUERY_TYPE);

        for (DataHandler dataHandler : dataHandlerList) {
            for (Map.Entry<String, DataFetcher> e : dataHandler.getDataFetcherMap().entrySet()) {
                builder.dataFetcher(e.getKey(), e.getValue());
            }
        }

        return builder.build();
    }

    public GraphQLSchema get() {
        return this.schema;
    }
}
