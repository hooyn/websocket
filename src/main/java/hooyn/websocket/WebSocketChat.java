package hooyn.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@ServerEndpoint(value = "/chat")
public class WebSocketChat {
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        log.info("Session Open... " + session.toString());
        if(!clients.contains(session)){
            clients.add(session);
            log.info("Session Open Success " + session);
        } else {
            log.error("Already Open Session");
        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception {
        log.info("Receive Message: " + msg);
        for (Session client : clients) {
            session.getBasicRemote().sendText(msg);
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("Session Close Success " + session);
        clients.remove(session);
    }
}
