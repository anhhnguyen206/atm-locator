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
    void search();
    void searchCenter(double lat, double lon);
    void searchRange(double range);
    void searchText(String searchText);
}
