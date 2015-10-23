package me.anhnguyen.atmfinder.viewmodel.atm.finder;

import java.util.List;

import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
public interface AtmFinderViewModel {
    Observable<List<Atm>> atms();
    Observable<Boolean> loading();
    Observable<String> error();
    BehaviorSubject<Double> searchLat();
    BehaviorSubject<Double> searchLon();
    BehaviorSubject<Double> searchRange();
    BehaviorSubject<String> searchText();

    void search();
}
