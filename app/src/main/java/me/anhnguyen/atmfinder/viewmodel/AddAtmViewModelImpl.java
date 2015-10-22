package me.anhnguyen.atmfinder.viewmodel;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.interactor.AddAtmInteractor;
import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;

/**
 * Created by nguyenhoanganh on 10/22/15.
 */
public class AddAtmViewModelImpl implements AddAtmViewModel {
    private BehaviorSubject<String> address = BehaviorSubject.create();
    private BehaviorSubject<String> name = BehaviorSubject.create();
    private BehaviorSubject<Double> lat = BehaviorSubject.create();
    private BehaviorSubject<Double> lon = BehaviorSubject.create();
    private BehaviorSubject<Boolean> loading = BehaviorSubject.create();
    private BehaviorSubject<Throwable> error = BehaviorSubject.create();
    private BehaviorSubject<Atm> atm = BehaviorSubject.create();

    private AddAtmInteractor addAtmInteractor;
    private Scheduler schedulerIo;
    private Scheduler schedulerUi;

    @Inject
    public AddAtmViewModelImpl(AddAtmInteractor addAtmInteractor,
                               @ForSchedulerIo Scheduler schedulerIo,
                               @ForSchedulerUi Scheduler schedulerUi) {
        this.addAtmInteractor = addAtmInteractor;
        this.schedulerIo = schedulerIo;
        this.schedulerUi = schedulerUi;
    }

    @Override
    public Observable<Boolean> canSave() {
        return Observable.
                combineLatest(address.asObservable(), name.asObservable(), lat.asObservable(), lon.asObservable(),
                        (address, name, lat, lon) -> address.length() > 0 && name.length() > 0 && lat != null && lon != null);

    }

    @Override
    public Observable<String> error() {
        return error.asObservable().map(throwable -> throwable.getMessage());
    }

    @Override
    public Observable<Boolean> loading() {
        return loading.asObservable();
    }

    @Override
    public Observable<Atm> done() {
        return atm.asObservable();
    }

    @Override
    public void setAddress(String address) {
        this.address.onNext(address);
    }

    @Override
    public void setName(String name) {
        this.name.onNext(name);
    }

    @Override
    public void setLat(double lat) {
        this.lat.onNext(lat);
    }

    @Override
    public void setLon(double lon) {
        this.lon.onNext(lon);
    }

    @Override
    public void add() {
        loading.onNext(Boolean.TRUE);
        addAtmInteractor.execute(name.getValue(), address.getValue(), lat.getValue(), lon.getValue())
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(atm -> {
                            this.atm.onNext(atm);
                            this.loading.onNext(Boolean.FALSE);
                        },
                        throwable -> {
                            this.error.onNext(throwable);
                            this.loading.onNext(Boolean.FALSE);
                        }
                );
    }
}
