package de.nodeline.box.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@EntityScan(basePackages = "de.nodeline.box")
public class TestContainersConfig {

    // Start a PostgreSQL Testcontainer
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.2")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    static {
        postgresContainer.start();
    }

    // Set dynamic properties for Spring Boot
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=create-drop", // Ensure schema is created
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
                    "spring.jpa.show-sql=true"
            ).applyTo(context.getEnvironment());
            System.out.println(context.getEnvironment().toString());
        }
    }
}

