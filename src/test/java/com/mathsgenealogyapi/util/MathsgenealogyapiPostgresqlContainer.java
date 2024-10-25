package com.mathsgenealogyapi.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testcontainers.containers.PostgreSQLContainer;

public class MathsgenealogyapiPostgresqlContainer extends PostgreSQLContainer<MathsgenealogyapiPostgresqlContainer> {

    private static final Logger logger = LogManager.getLogger(MathsgenealogyapiPostgresqlContainer.class);

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
        logger.info("Started Postgresql container " + container.getJdbcUrl() + ", U: " + container.getUsername() + " P: " + container.getPassword());

    }

    @Override
    public void stop() {

    }
}
