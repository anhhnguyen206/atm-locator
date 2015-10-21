package me.anhnguyen.atmfinder.viewmodel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractor;
import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;
import rx.Scheduler;
import rx.subjects.BehaviorSubject;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AtmFinderViewModelImpl implements AtmFinderViewModel {
    private BehaviorSubject<List<Atm>> atms = BehaviorSubject.create(Collections.<Atm>emptyList());
    private BehaviorSubject<Boolean> loading = BehaviorSubject.create(Boolean.FALSE);
    private BehaviorSubject<Throwable> error = BehaviorSubject.create();
    private BehaviorSubject<Double> lat = BehaviorSubject.create();
    private BehaviorSubject<Double> lon = BehaviorSubject.create();

    private FindAtmInteractor findAtmInteractor;
    private Scheduler schedulerIo;
    private Scheduler schedulerUi;

    @Inject
    public AtmFinderViewModelImpl(FindAtmInteractor findAtmInteractor,
                                  @ForSchedulerIo Scheduler schedulerIo,
                                  @ForSchedulerUi Scheduler schedulerUi) {
        this.findAtmInteractor = findAtmInteractor;
        this.schedulerIo = schedulerIo;
        this.schedulerUi = schedulerUi;
    }

    @Override
    public Observable<List<Atm>> atms() {
        return atms;
    }

    @Override
    public Observable<Boolean> loading() {
        return loading;
    }

    @Override
    public Observable<String> error() {
        return error.map(throwable -> throwable.getMessage());
    }

    @Override
    public void search(String name, double range) {
        loading.onNext(Boolean.TRUE);
        findAtmInteractor.execute(name, range, lat.getValue(), lon.getValue())
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(
                        atms -> this.atms.onNext(atms),
                        throwable -> this.error.onNext(throwable),
                        () -> this.loading.onNext(Boolean.FALSE));
    }

    @Override
    public void searchCenterLocation(double lat, double lon) {
        this.lat.onNext(lat);
        this.lon.onNext(lon);
    }
}
