package me.anhnguyen.atmfinder.repository;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.dao.query.QueryBuilder;
import me.anhnguyen.atmfinder.Constants;
import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.model.dao.AtmDao;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AtmRepositoryImpl implements AtmRepository {
    AtmDao atmDao;
    final double earthRadius = Constants.EARTH_RADIUS; // m
    final double mult = 1.1;

    @Inject
    public AtmRepositoryImpl(AtmDao atmDao) {
        this.atmDao = atmDao;
    }

    @Override
    public Atm insert(Atm atm) {
        long id = atmDao.insert(atm);
        return atmDao.load(id);
    }

    @Override
    public List<Atm> findNearbyAtms(String name, double lat, double lon, double range) {
        LatLng center = new LatLng(lat, lon);
        LatLng southPoint = SphericalUtil.computeOffset(center, range * Math.sqrt(2.0), 0);
        LatLng westPoint = SphericalUtil.computeOffset(center, range * Math.sqrt(2.0), 90);
        LatLng northPoint = SphericalUtil.computeOffset(center, range * Math.sqrt(2.0), 180);
        LatLng eastPoint = SphericalUtil.computeOffset(center, range * Math.sqrt(2.0), 270);
        QueryBuilder<Atm> queryBuilder = atmDao.queryBuilder();
        queryBuilder.where(AtmDao.Properties.Name.like("%" + name + "%"));
        queryBuilder.where(AtmDao.Properties.Lat.gt(northPoint.latitude));
        queryBuilder.where(AtmDao.Properties.Lat.lt(southPoint.latitude));
        queryBuilder.where(AtmDao.Properties.Lon.lt(westPoint.longitude));
        queryBuilder.where(AtmDao.Properties.Lon.gt(eastPoint.longitude));
        queryBuilder.orderRaw(" abs(lat - " + lat + ") + abs(lon - " + lon + ")");

        List<Atm> atms = queryBuilder.list();
        List<Atm> refinedResults = new ArrayList<>();

        for (Atm atm : atms) {
            double distanceFromCenterToAtm = SphericalUtil.computeDistanceBetween(center, new LatLng(atm.getLat(), atm.getLon()));
            if (distanceFromCenterToAtm <= range) {
                refinedResults.add(atm);
            }
        }

        return refinedResults;
    }
}
