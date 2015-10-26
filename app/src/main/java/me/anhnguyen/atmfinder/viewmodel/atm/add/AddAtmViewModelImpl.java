package me.anhnguyen.atmfinder.viewmodel.atm.add;

import com.google.android.gms.maps.model.LatLng;

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
    private BehaviorSubject<LatLng> latLng = BehaviorSubject.create();
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
                combineLatest(address(), name(), latLng(),
                        (address, name, latLng) -> address.length() > 0 && name.length() > 0 && latLng != null);

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
    public Observable<String> address() {
        return address.asObservable();
    }

    @Override
    public Observable<String> name() {
        return name.asObservable();
    }

    @Override
    public Observable<LatLng> latLng() {
        return latLng.asObservable();
    }

    @Override
    public void setAddress(String address) {
        if (this.address.getValue() == null || !this.address.getValue().equals(address)) {
            this.address.onNext(address);
        }
    }

    @Override
    public void setName(String name) {
        if (this.name.getValue() == null || !this.name.getValue().equals(name)) {
            this.name.onNext(name);
        }
    }

    @Override
    public void setLatLng(LatLng latLng) {
        LatLng currentLatLng = this.latLng.getValue();

        if (currentLatLng != null) {
            if (currentLatLng.latitude != latLng.latitude || currentLatLng.longitude != latLng.longitude) {
                this.latLng.onNext(latLng);
            }
        } else {
            this.latLng.onNext(latLng);
        }
    }

    @Override
    public void add() {
        loading.onNext(Boolean.TRUE);
        addAtmInteractor.execute(name.getValue(), address.getValue(), latLng.getValue().latitude, latLng.getValue().longitude)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(atm1 -> {
                            AddAtmViewModelImpl.this.atm.onNext(atm1);
                            AddAtmViewModelImpl.this.loading.onNext(Boolean.FALSE);
                        },
                        throwable -> {
                            AddAtmViewModelImpl.this.error.onNext(throwable);
                            AddAtmViewModelImpl.this.loading.onNext(Boolean.FALSE);
                        }
                );
    }
}
