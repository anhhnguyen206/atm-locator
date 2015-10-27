package me.anhnguyen.atmfinder;

import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.model.dao.AtmDao;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
public class AtmRepositoryTestHelper {
    private AtmDao atmDao;

    public AtmRepositoryTestHelper(AtmDao atmDao) {
        this.atmDao = atmDao;
    }

    public void createAtmListNearbySearch() {
        Atm atm = createEntity(1l);
        atm.setName("Vietcombank chi nhánh Quang Trung Gò Vấp");
        atm.setAddress("49 Quang Trung, 10, Gò Vấp, Hồ Chí Minh, Vietnam");
        atm.setLat(10.8389326);
        atm.setLon(106.661651);

        atmDao.insert(atm);

        atm = createEntity(2l);
        atm.setName("Citibank Võ Văn Tần");
        atm.setAddress("34-34A, Võ Văn Tần, phường 6, Hồ Chí Minh, Vietnam");
        atm.setLat(10.778656);
        atm.setLon(106.6221091);

        atmDao.insert(atm);

        atm = createEntity(3l);
        atm.setName("Dong A Bank Hà Huy Giáp");
        atm.setAddress("540 Hà Huy Giáp, Thạnh Lộc, 12, Hồ Chí Minh, Vietnam");
        atm.setLat(10.877963);
        atm.setLon(106.6776794);

        atmDao.insert(atm);
    }

    private Atm createEntity(Long key) {
        return new Atm(key);
    }
}
