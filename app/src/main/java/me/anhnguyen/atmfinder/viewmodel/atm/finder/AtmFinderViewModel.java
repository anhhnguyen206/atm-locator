package me.anhnguyen.atmfinder.viewmodel.atm.finder;

import java.util.List;

import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
public interface AtmFinderViewModel {
    Observable<List<Atm>> atms();
    Observable<Boolean> loading();
    Observable<String> error();

    // use Integer to eliminate dependency to Context.getResource().getString()
    Observable<Integer> infoResId();
    Observable<Double> lat();
    Observable<Double> lon();
    Observable<Double> range();
    Observable<String> keyword();

    void setLat(double lat);
    void setLon(double lon);
    void setRange(double range);
    void setKeyword(String keyword);
    void search();
}
