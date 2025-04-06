package com.dropwizard;

import com.dropwizard.api.SessionResource;
import com.dropwizard.auth.ApiKeyAuthFilter;
import com.dropwizard.db.SessionDAO;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jdbi3.JdbiFactory;
import org.jdbi.v3.core.Jdbi;

/**
 * Main application class for the SessionService Dropwizard application.
 * Sets up the database connection, REST resources, and authentication filter.
 */
public class SessionServiceApplication extends io.dropwizard.core.Application<SessionServiceConfiguration> {

    /**
     * Entry point to configure and start the application.
     *
     * @param config Configuration object loaded from config.yml
     * @param env    Dropwizard environment object
     * @throws Exception if application fails to start
     */
    @Override
    public void run(SessionServiceConfiguration config, Environment env) throws Exception {
        // Create JDBI factory for connecting to MySQL
        final JdbiFactory factory = new JdbiFactory();

        // Build Jdbi instance using the DataSourceFactory and naming the connection "mysql"
        final Jdbi jdbi = factory.build(env, config.getDataSourceFactory(), "mysql");

        // Create a DAO implementation for managing session data
        final SessionDAO dao = jdbi.onDemand(SessionDAO.class);

        // Register REST resource that handles session endpoints
        env.jersey().register(new SessionResource(dao));

        // Register authentication filter to validate API key from requests
        env.jersey().register(new ApiKeyAuthFilter(config.getApiKey()));
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments
     * @throws Exception if application fails to start
     */
    public static void main(String[] args) throws Exception {
        new SessionServiceApplication().run(args);
    }

    /**
     * Optional initializer for adding bundles or configuration at bootstrap time.
     *
     * @param bootstrap Dropwizard bootstrap instance
     */
    @Override
    public void initialize(Bootstrap<SessionServiceConfiguration> bootstrap) {
        // You can register additional bundles or configurations here if needed.
        // bootstrap.addBundle(new ServerBundle());
    }
}
