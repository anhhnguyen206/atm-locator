package me.anhnguyen.atmfinder.viewmodel.atm.add;

import com.google.android.gms.maps.model.LatLng;

import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/22/15.
 */
public interface AddAtmViewModel {
    Observable<Boolean> canSave();
    Observable<String> error();
    Observable<Boolean> loading();
    Observable<Atm> done();
    Observable<String> address();
    Observable<String> name();
    Observable<LatLng> latLng();

    void setAddress(String address);
    void setName(String name);
    void setLatLng(LatLng latLng);
    void add();
}
