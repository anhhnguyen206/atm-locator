package me.anhnguyen.atmfinder.interactor;

import java.util.List;

import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/20/15.
 */
public interface FindAtmInteractor {
    Observable<List<Atm>> execute(String name, double centerLat, double centerLon, double range);
}