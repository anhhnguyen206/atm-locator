package me.anhnguyen.atmfinder.repository;

import java.util.List;

import me.anhnguyen.atmfinder.model.dao.Atm;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public interface AtmRepository {
    Atm insert(Atm atm);
    List<Atm> findNearbyAtms(String name, double lat, double lon, double range);
}
