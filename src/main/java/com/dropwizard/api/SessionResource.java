package com.dropwizard.api;

import com.dropwizard.db.SessionDAO;
import com.dropwizard.model.Session;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * RESTful resource class to manage conference sessions.
 * Supports standard CRUD operations on the /sessions endpoint.
 */
@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionResource.class);

    private final SessionDAO dao;

    /**
     * Constructor injection of DAO to interact with the database.
     */
    public SessionResource(SessionDAO dao) {
        this.dao = dao;
    }

    /**
     * Creates a new session.
     *
     * @param session the session data to be added.
     * @return Response with status 201 and generated session ID.
     */
    @POST
    public Response create(@Valid Session session) {
        int id = dao.insert(session);
        LOGGER.info("Created session with ID: {}", id);
        return Response.status(Response.Status.CREATED)
                .entity(Collections.singletonMap("id", id))
                .build();
    }

    /**
     * Retrieves a specific session by its ID.
     *
     * @param id the session ID.
     * @return the session object or 404 if not found.
     */
    @GET
    @Path("/{id}")
    public Session get(@PathParam("id") int id) {
        LOGGER.debug("Fetching session with ID: {}", id);
        return Optional.ofNullable(dao.findById(id))
                .orElseThrow(() -> new NotFoundException("Session not found for ID: " + id));
    }

    /**
     * Retrieves a paginated list of sessions.
     *
     * @param offset the starting index (default is 0).
     * @param limit  the number of sessions to retrieve (default is 10).
     * @return list of sessions.
     */
    @GET
    public List<Session> list(@QueryParam("offset") @DefaultValue("0") int offset,
                              @QueryParam("limit") @DefaultValue("10") int limit) {
        LOGGER.debug("Fetching sessions with offset: {}, limit: {}", offset, limit);
        return dao.findPaginated(offset, limit);
    }

    /**
     * Updates an existing session by ID.
     *
     * @param id      the session ID.
     * @param session the updated session object.
     * @return HTTP 200 OK if successful.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, @Valid Session session) {
        requireExistingSession(id, "update");
        session.setId(id);
        dao.update(session);
        LOGGER.info("Session with ID {} updated successfully.", id);
        return Response.ok().build();
    }

    /**
     * Deletes a session by ID.
     *
     * @param id the session ID.
     * @return HTTP 204 No Content if successful.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        requireExistingSession(id, "delete");
        dao.delete(id);
        LOGGER.info("Session with ID {} deleted successfully.", id);
        return Response.noContent().build();
    }

    /**
     * Helper method to ensure a session exists before update or delete.
     *
     * @param id     the session ID.
     * @param action the attempted action (used in logs and messages).
     * @throws NotFoundException if session is not found.
     */
    private void requireExistingSession(int id, String action) {
        Session existing = dao.findById(id);
        if (existing == null) {
            LOGGER.warn("Failed to {}. Session with ID {} not found.", action, id);
            throw new NotFoundException("Session not found for " + action);
        }
    }
}
