package me.anhnguyen.atmfinder.viewmodel;

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

    void setAddress(String address);
    void setName(String name);
    void setLat(double lat);
    void setLon(double lon);
    void add();
}
