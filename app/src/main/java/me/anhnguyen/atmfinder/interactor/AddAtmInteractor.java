package me.anhnguyen.atmfinder.interactor;

import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public interface AddAtmInteractor {
    Observable<Atm> execute(String name, String address, double lat, double lon);
}
