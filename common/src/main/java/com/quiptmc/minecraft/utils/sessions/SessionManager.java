package com.quiptmc.minecraft.utils.sessions;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;

import java.util.UUID;

public class SessionManager {

    private static final Registry<Session> sessions = Registries.register("sessions", () -> null);

    public static Session get(String sessionId) {
        return sessions.get(sessionId).orElseThrow(() -> new IllegalArgumentException("Session " + sessionId + " not found"));
    }

    public static Session register(Session.Data sessionData) {
        UUID id = UUID.randomUUID();
        while(sessions.get(id.toString()).isPresent()) {
            id = UUID.randomUUID(); // Ensure unique session ID
        }
        Session session = sessionData.type().equals(Session.Type.GLOBAL) ? new Session(id) : new Session(id, sessionData.player());
        sessions.register(id.toString(), session);
        return session;
    }
}
