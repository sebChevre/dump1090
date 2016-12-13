package ch.sebooom.dump1090;

import org.junit.Test;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */

public class RxBusTest {

    class Bus {
        private final Subject<String, String> bus = new SerializedSubject<>(PublishSubject.create());

        public void send(String message) {
            bus.onNext(message);
        }

        public Observable<String> toObserverable() {
            return bus;
        }
    }

    @Test
    public void busMsgTest () {

        Bus bus = new Bus();

        bus.send("one");
        bus.send("two");
        bus.send("three");

        bus.toObserverable().buffer(3).subscribe(allMessages -> {
            assertTrue(allMessages.size() == 3);

        });


        bus.toObserverable().buffer(3).subscribe(allMessages -> {
            assertTrue(allMessages.size() == 3);

        });


        bus.toObserverable().buffer(3).subscribe(allMessages -> {
            assertTrue(allMessages.size() == 3);

        });



    }
}
