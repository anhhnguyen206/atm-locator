package me.anhnguyen.atmfinder.viewmodel;

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
    void search(String name, double range);
    void searchCenterLocation(double lat, double lon);
}
