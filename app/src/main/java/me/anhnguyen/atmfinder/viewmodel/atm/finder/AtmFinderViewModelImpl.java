package me.anhnguyen.atmfinder.viewmodel.atm.finder;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.Constants;
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
    private BehaviorSubject<Integer> infoResId = BehaviorSubject.create();

    private BehaviorSubject<LatLng> latLng = BehaviorSubject.create();
    private BehaviorSubject<String> searchText = BehaviorSubject.create("");

    // default search range to be 2000 m
    private BehaviorSubject<Double> searchRange = BehaviorSubject.create(Double.valueOf(2000));

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
    public Observable<Integer> infoResId() {
        return infoResId.asObservable();
    }

    @Override
    public Observable<LatLng> latLng() {
        return latLng.asObservable();
    }

    @Override
    public Observable<Double> range() {
        return searchRange.asObservable();
    }

    @Override
    public Observable<String> keyword() {
        return searchText.asObservable();
    }

    @Override
    public Observable<Boolean> drawCircle() {
        return Observable.combineLatest(range(), latLng(), (range, latLng) -> range != null && latLng != null);
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
    public void setRange(double range) {
        if (searchRange.getValue() != range) {
            this.searchRange.onNext(range);
        }
    }

    @Override
    public void setKeyword(String keyword) {
        this.searchText.onNext(keyword);
    }

    @Override
    public void search() {
        List<Double> possibleRanges = Constants.RANGES.subList(Constants.RANGES.indexOf(searchRange.getValue()), Constants.RANGES.size());

        Observable.from(possibleRanges)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .doOnNext(range -> loading.onNext(Boolean.TRUE))
                .doOnNext(range -> this.searchRange.onNext(range))
                .flatMap(range -> findAtmInteractor.execute(searchText.getValue(), latLng.getValue().latitude, latLng.getValue().longitude, range))
                .takeUntil(atms -> atms.size() > 0)
                .subscribe(
                        atms -> {
                            AtmFinderViewModelImpl.this.atms.onNext(atms);
                            AtmFinderViewModelImpl.this.loading.onNext(Boolean.FALSE);
                        },
                        throwable -> {
                            AtmFinderViewModelImpl.this.error.onNext(throwable);
                            AtmFinderViewModelImpl.this.loading.onNext(Boolean.FALSE);
                        }
                );

    }

    @Override
    public LatLng getLatLng() {
        return latLng.getValue();
    }

    @Override
    public double getRange() {
        return searchRange.getValue();
    }

    @Override
    public String getKeyword() {
        return searchText.getValue();
    }
}
