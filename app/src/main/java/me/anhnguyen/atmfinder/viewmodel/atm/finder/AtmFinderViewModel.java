package me.anhnguyen.atmfinder.viewmodel.atm.finder;

import com.google.android.gms.maps.model.LatLng;

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
    Observable<LatLng> latLng();
    Observable<Double> range();
    Observable<String> keyword();
    Observable<Boolean> drawCircle();

    void setLatLng(LatLng latLng);
    void setRange(double range);
    void setKeyword(String keyword);
    void search();

    LatLng getLatLng();
    double getRange();
    String getKeyword();
}
