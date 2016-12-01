package ch.sebooom.dump1090;

import ch.sebooom.dump1090.utils.Utils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Handler pour le server Spark permettant le traitement des websocket
 *
 */
@WebSocket
class Dump1090WebSocketHandler {

    private final static Logger logger = Utils.getFileLogger(Dump1090WebSocketHandler.class.getName());
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private final RxBus bus;

    public Dump1090WebSocketHandler(RxBus bus) {
        this.bus = bus;
    }


    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);

        bus.toObserverable()
                .subscribe(next -> {
                    try {
                        logger.info("From bus: " + next);
                        session.getRemote().sendString(next.toJson());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        }, error -> logger.severe(error.getMessage())
                        , () -> logger.info("Stream terminated. This is a problem in this context..."));
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }


}
