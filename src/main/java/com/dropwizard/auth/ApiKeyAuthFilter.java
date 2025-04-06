package com.dropwizard.auth;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * A JAX-RS request filter that performs simple API key-based authentication.
 * This filter checks the "X-API-Key" header in incoming requests and compares
 * it with the expected API key.
 */
@Provider // Marks this class as a provider that can be automatically discovered and registered
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    private final String expectedApiKey;

    /**
     * Constructor that takes the expected API key (configured in application config).
     *
     * @param expectedApiKey The valid API key to compare against incoming requests.
     */
    public ApiKeyAuthFilter(String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    /**
     * Filters each incoming request and checks for the "X-API-Key" header.
     * If the header is missing or doesn't match, it aborts the request with 401 Unauthorized.
     *
     * @param requestContext The context of the incoming request.
     * @throws IOException If an error occurs during filtering.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String apiKey = requestContext.getHeaderString("X-API-Key");

        // Abort the request if API key is missing or invalid
        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("API key missing or invalid")
                            .build()
            );
        }
    }
}
