package org.euler.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.euler.resource.QueryResource;
import org.euler.filter.MDCFilter;

public class EulerApplication extends Application<EulerApplicationConfig> {

    public static void main(String[] args) throws Exception {
        new EulerApplication().run(args);
    }

    @Override
    public void run(EulerApplicationConfig eulerApplicationConfig, Environment environment) {
        Injector injector = this.getInjector(eulerApplicationConfig, environment);

        environment.jersey().register(injector.getInstance(QueryResource.class));
        environment.healthChecks().register("healthcheck", injector.getInstance(EulerApplicationHealthCheck.class));
        environment.jersey().register(injector.getInstance(MDCFilter.class));
    }

    private Injector getInjector(EulerApplicationConfig configuration, Environment environment) {
        return Guice.createInjector(
                new GuiceModule(configuration, environment)
        );
    }
}
