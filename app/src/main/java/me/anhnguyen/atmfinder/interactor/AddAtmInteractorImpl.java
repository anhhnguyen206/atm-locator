package me.anhnguyen.atmfinder.interactor;

import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AddAtmInteractorImpl implements AddAtmInteractor {
    private AtmRepository atmRepository;

    public AddAtmInteractorImpl(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    public Observable<Atm> execute(String name, String address, double lat, double lon) {
        return Observable.create(subscriber -> {
            try {
                Atm atm = new Atm();
                atm.setName(name);
                atm.setAddress(address);
                atm.setLat(lat);
                atm.setLon(lon);
                subscriber.onNext(atmRepository.insert(atm));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        });
    }
}
