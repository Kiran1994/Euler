package org.euler.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import graphql.GraphQL;
import io.dropwizard.setup.Environment;
import org.euler.data.graphql.EulerGraphQLSchema;
import org.euler.data.graphql.handler.DataHandler;
import org.euler.data.graphql.handler.UserHandler;
import org.euler.resource.QueryResource;
import org.euler.service.QueryExecutionService;
import org.euler.utils.Constants;

import java.util.Arrays;
import java.util.List;

public class GuiceModule extends AbstractModule {

    private final EulerApplicationConfig eulerApplicationConfig;
    private final Environment environment;

    public GuiceModule(EulerApplicationConfig eulerApplicationConfig, Environment environment) {
        this.eulerApplicationConfig = eulerApplicationConfig;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(EulerGraphQLSchema.class).in(Singleton.class);
        bind(QueryResource.class).in(Singleton.class);

        Injector injector = Guice.createInjector(this);
        injector.getInstance(QueryExecutionService.class);
    }

    @Provides
    @Singleton
    GraphQL provideGraphQL(EulerGraphQLSchema eulerGraphQLSchema) {
        return new GraphQL.Builder(eulerGraphQLSchema.get()).build();
    }

    @Named(Constants.Guice.DATA_HANDLERS)
    @Provides
    @Singleton
    List<DataHandler> provideDataHandlers(UserHandler userHandler) {
        return Arrays.asList(userHandler);
    }
}
