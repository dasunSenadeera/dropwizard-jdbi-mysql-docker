
package com.dropwizard.api;

import com.dropwizard.db.SessionDAO;
import com.dropwizard.model.Session;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;



public class SessionResourceTest {

    private SessionDAO dao;
    private SessionResource resource;

    @BeforeEach
    public void setUp() {
        dao = mock(SessionDAO.class);
        resource = new SessionResource(dao);
    }

    @Test
    public void testCreate() {
        Session session = new Session();
        when(dao.insert(session)).thenReturn(1);

        Response response = resource.create(session);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(1, ((java.util.Map<?, ?>) response.getEntity()).get("id"));
    }

    @Test
    public void testGetFound() {
        Session session = new Session();
        session.setId(1);
        when(dao.findById(1)).thenReturn(session);

        Session result = resource.get(1);
        assertEquals(1, result.getId());
    }

    @Test
    public void testGetNotFound() {
        when(dao.findById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> resource.get(1));
    }

    @Test
    public void testList() {
        List<Session> sessions = Collections.singletonList(new Session());
        when(dao.findPaginated(0, 10)).thenReturn(sessions);

        List<Session> result = resource.list(0, 10);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateFound() {
        Session session = new Session();
        session.setId(1);
        when(dao.findById(1)).thenReturn(session);

        Response response = resource.update(1, session);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(dao).update(session);
    }

    @Test
    public void testUpdateNotFound() {
        when(dao.findById(1)).thenReturn(null);
        Session session = new Session();
        assertThrows(NotFoundException.class, () -> resource.update(1, session));
    }

    @Test
    public void testDeleteFound() {
        Session session = new Session();
        when(dao.findById(1)).thenReturn(session);

        Response response = resource.delete(1);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(dao).delete(1);
    }

    @Test
    public void testDeleteNotFound() {
        when(dao.findById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> resource.delete(1));
    }
}
