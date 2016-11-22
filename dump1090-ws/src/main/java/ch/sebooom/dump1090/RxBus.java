package ch.sebooom.dump1090;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by seb on 22.11.16.
 */
public class RxBus {


    private final Subject<String, String> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(String o) {
        System.out.println("bus on next");
        _bus.onNext(o);
    }

    public Observable<String> toObserverable() {
        return _bus;
    }
}
