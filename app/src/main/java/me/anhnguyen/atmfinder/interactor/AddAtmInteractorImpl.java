package me.anhnguyen.atmfinder.interactor;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.model.dao.AtmDao;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AddAtmInteractorImpl implements AddAtmInteractor {
    @Inject
    AtmDao atmDao;

    @Override
    public Observable<Atm> execute(String name, String address, double lat, double lon) {
        return Observable.create(subscriber -> {
            Atm atm = new Atm();
            atm.setName(name);
            atm.setAddress(address);
            atm.setLat(lat);
            atm.setLon(lon);
        });
    }
}
