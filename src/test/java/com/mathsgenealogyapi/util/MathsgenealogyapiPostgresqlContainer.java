package com.mathsgenealogyapi.util;

import org.testcontainers.containers.PostgreSQLContainer;

public class MathsgenealogyapiPostgresqlContainer extends PostgreSQLContainer<MathsgenealogyapiPostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:12";

    private static MathsgenealogyapiPostgresqlContainer container;

    private MathsgenealogyapiPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static MathsgenealogyapiPostgresqlContainer getInstance() {
        if (container == null) {
            container = new MathsgenealogyapiPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {

    }
}
