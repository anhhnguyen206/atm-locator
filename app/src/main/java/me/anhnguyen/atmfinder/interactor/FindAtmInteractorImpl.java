package me.anhnguyen.atmfinder.interactor;

import java.util.List;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/20/15.
 */
public class FindAtmInteractorImpl implements FindAtmInteractor {
    private AtmRepository atmRepository;

    @Inject
    public FindAtmInteractorImpl(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @Override
    public Observable<List<Atm>> execute(String name, double centerLat, double centerLon, double range) {
        return Observable.just(atmRepository.findNearbyAtms(name, centerLat, centerLon, range));
    }
}
