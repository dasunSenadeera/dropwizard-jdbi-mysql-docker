package com.dropwizard.db;

import com.dropwizard.model.Session;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on the 'sessions' table.
 * Uses JDBI's SQL Object API to map methods to SQL queries.
 */
@RegisterBeanMapper(Session.class) // Automatically maps SQL result sets to Session objects
public interface SessionDAO {

    /**
     * Inserts a new session into the database.
     * @param session The session object containing title, description, speakerName, and fileUploadUrl.
     * @return The auto-generated primary key (id) of the inserted session.
     */
    @SqlUpdate("INSERT INTO sessions (title, description, speaker_name, file_upload_url) VALUES (:title, :description, :speakerName, :fileUploadUrl)")
    @GetGeneratedKeys
    int insert(@BindBean Session session);

    /**
     * Retrieves a session by its ID.
     * @param id The unique ID of the session.
     * @return The session object if found, otherwise null.
     */
    @SqlQuery("SELECT * FROM sessions WHERE id = :id")
    @RegisterBeanMapper(Session.class)
    Session findById(@Bind("id") int id);

    /**
     * Retrieves a paginated list of sessions.
     * @param offset The starting index.
     * @param limit The maximum number of sessions to retrieve.
     * @return A list of sessions.
     */
    @SqlQuery("SELECT * FROM sessions ORDER BY id LIMIT :limit OFFSET :offset")
    List<Session> findPaginated(@Bind("offset") int offset, @Bind("limit") int limit);

    /**
     * Retrieves all sessions from the database.
     * @return A list of all sessions.
     */
    @SqlQuery("SELECT * FROM sessions")
    @RegisterBeanMapper(Session.class)
    List<Session> findAll();

    /**
     * Updates an existing session in the database.
     * The session is matched by ID.
     * @param session The session object containing updated data.
     */
    @SqlUpdate("UPDATE sessions SET title = :title, description = :description, speaker_name = :speakerName, file_upload_url = :fileUploadUrl WHERE id = :id")
    void update(@BindBean Session session);

    /**
     * Deletes a session from the database using its ID.
     * @param id The ID of the session to delete.
     */
    @SqlUpdate("DELETE FROM sessions WHERE id = :id")
    void delete(@Bind("id") int id);

    /**
     * Counts the total number of sessions in the table.
     * @return The total count of sessions.
     */
    @SqlQuery("SELECT COUNT(*) FROM sessions")
    int countAll();
}
