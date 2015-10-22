package me.anhnguyen.atmfinder.interactor;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AddAtmInteractorImpl implements AddAtmInteractor {
    private AtmRepository atmRepository;

    @Inject
    public AddAtmInteractorImpl(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    public Observable<Atm> execute(String name, String address, double lat, double lon) {
        return Observable.create(subscriber -> {
            try {
                if (name.isEmpty()) {
                    throw new Exception("Name cannot be empty");
                }

                if (address.isEmpty()) {
                    throw new Exception("Address cannot be empty");
                }

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
