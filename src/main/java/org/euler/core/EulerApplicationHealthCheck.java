package org.euler.core;

import com.codahale.metrics.health.HealthCheck;

public class EulerApplicationHealthCheck extends HealthCheck {

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
