package com.hascode.tutorial;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{room}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatEndpoint {
	private final Logger log = Logger.getLogger(getClass().getName());
    private static Integer iMsgCnt = 0;
    private static Map<Session, String> msg_sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void open(final Session session, @PathParam("room") final String room) {
		log.info("session openend and bound to room: " + room);
		session.getUserProperties().put("room", room);
        msg_sessions.put(session, room);
	}

	@OnMessage
	public void onMessage(final Session session, final ChatMessage chatMessage) {
		String room = (String) session.getUserProperties().get("room");
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen() && room.equals(s.getUserProperties().get("room"))) {
					s.getBasicRemote().sendObject(chatMessage);
                    iMsgCnt++;

                    // Рассылка сообщения всем подключившимся
                    for(Map.Entry<Session, String> entry : msg_sessions.entrySet()) {
                        Session key = entry.getKey();
                        String val = entry.getValue();

                        if (!key.equals(session) && val.equals(s.getUserProperties().get("room")))
                            key.getBasicRemote().sendObject(chatMessage);
                    }
                }
			}
		} catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
		}
	}

    @OnClose
	public void onClose(Session session, CloseReason closeReason)
    {
        log.info("Session " + session.getId() + " closed");
        msg_sessions.remove(session);
    }
}
