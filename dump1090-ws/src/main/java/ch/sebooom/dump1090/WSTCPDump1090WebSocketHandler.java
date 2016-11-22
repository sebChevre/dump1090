package ch.sebooom.dump1090;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by seb on 22.11.16.
 */
@WebSocket
public class WSTCPDump1090WebSocketHandler {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private final RxBus bus;

    public WSTCPDump1090WebSocketHandler(RxBus bus) {
        this.bus = bus;
    }


    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);

        bus.toObserverable()
                .subscribe(next -> {
                    try {
                        System.out.println("ws publish");
                        session.getRemote().sendString(next);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
    }
}
