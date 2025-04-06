package com.dropwizard;

import io.dropwizard.core.server.DefaultServerFactory;
import io.dropwizard.core.server.ServerFactory;
import io.dropwizard.db.DataSourceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Configuration class for the Dropwizard application.
 * Holds server, database, and API key configurations.
 */
public class SessionServiceConfiguration extends Configuration {

    /**
     * Configuration for the application's database.
     * Populated from the YAML config under the 'database' section.
     */
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    /**
     * Configuration for the Dropwizard HTTP server.
     * Populated from the YAML config under the 'server' section.
     */
    @Valid
    @NotNull
    private ServerFactory server = new DefaultServerFactory();

    /**
     * API key used to authenticate incoming HTTP requests.
     * Populated from the YAML config under the 'apiKey' field.
     */
    @NotEmpty
    private String apiKey;

    /**
     * Gets the configured API key.
     * @return the expected API key for request authentication.
     */
    @JsonProperty
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API key from YAML configuration.
     * @param apiKey the API key to set.
     */
    @JsonProperty
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets the server factory configuration.
     * @return the server configuration object.
     */
    @JsonProperty("server")
    public ServerFactory getServerFactory() {
        return server;
    }

    /**
     * Sets the server factory configuration.
     * @param server the server configuration object.
     */
    @JsonProperty("server")
    public void setServerFactory(ServerFactory server) {
        this.server = server;
    }

    /**
     * Gets the database configuration.
     * @return the database configuration object.
     */
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    /**
     * Sets the database configuration.
     * @param dataSourceFactory the database configuration object.
     */
    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
}
