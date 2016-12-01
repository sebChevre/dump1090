package ch.sebooom.dump1090;

import ch.sebooom.dump1090.tcp.messages.Message;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.logging.Logger;

/**
 * Created by seb on 22.11.16.
 */
public class RxBus {

    final static Logger logger = Logger.getLogger(RxBus.class.getName());
    private final Subject<Message, Message> bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Message message) {
        logger.fine("Send to bus: " + message);
        bus.onNext(message);
    }

    public Observable<Message> toObserverable() {
        return bus;
    }
}
