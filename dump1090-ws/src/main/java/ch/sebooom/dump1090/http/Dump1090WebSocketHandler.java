package ch.sebooom.dump1090.http;

import ch.sebooom.dump1090.bus.RxBus;
import ch.sebooom.dump1090.log.EventType;
import ch.sebooom.dump1090.log.JsonLog;
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
public class Dump1090WebSocketHandler {

    private final static Logger logger = Logger.getLogger(Dump1090WebSocketHandler.class.getName());
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private final RxBus bus;

    public Dump1090WebSocketHandler(RxBus bus) {
        this.bus = bus;
    }


    @OnWebSocketConnect
    public void connected(Session session) {

        logger.info(JsonLog.technical(
                String.format("WebSocket Client connected: %s", session.getRemoteAddress())
                ,EventType.WEBSOCKET_CONNECTION,0));

        sessions.add(session);

        bus.toObserverable()
                .subscribe(next -> {
                    try {
                        logger.fine(JsonLog.technical(
                            String.format("From bus: %s", next),
                            EventType.INTERNAL_BUS,0));

                        if (session.isOpen()) {
                            logger.fine(
                                JsonLog.technical(
                                        String.format("Message sending to websocket: %s", next),
                                        EventType.WEBSOCKET_SENDING,0)
                            );
                            session.getRemote().sendString(next.toJson());
                        }
                    } catch (IOException e) {
                        logger.severe(
                            JsonLog.technical(
                                String.format("Client seems to be deconnected: %s", e.getMessage())
                                ,EventType.WEBSOCKET_SENDING,0)
                        );
                    }
            },
            error ->{
                logger.severe(
                    JsonLog.technical(
                        String.format("Client seems to be deconnected: %s", error.getMessage()),
                        EventType.WEBSOCKET_SENDING,0)
                );
            },
            () -> {
                logger.severe(
                    JsonLog.technical(
                        "Stream terminated. This is a problem in this context...",
                        EventType.WEBSOCKET_SENDING,0)
                );
            });

    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {

        logger.info(
            JsonLog.technical(
                "Client close websocket",
                EventType.WEBSOCKET_DISCONNECT,0)
            );

        sessions.remove(session);
    }


}
