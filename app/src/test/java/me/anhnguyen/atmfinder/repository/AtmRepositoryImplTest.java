package me.anhnguyen.atmfinder.repository;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;
import me.anhnguyen.atmfinder.BuildConfig;
import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.model.dao.AtmDao;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AtmRepositoryImplTest extends AbstractDaoTestLongPk<AtmDao, Atm> {
    private AtmRepositoryImpl atmRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        atmRepository = new AtmRepositoryImpl(this.dao);
    }

    public AtmRepositoryImplTest() {
        super(AtmDao.class);
    }

    @Override
    protected Atm createEntity(Long key) {
        return new Atm(key);
    }

    @Test
    public void atmShouldBeCreated() {
        Atm atm = createAtmForInsertion();

        atmRepository.insert(atm);

        Atm newlyInserted = dao.load(1l);
        assertNotNull(dao.load(1l));
        assertEquals("Vietcombank chi nhánh Quang Trung Gò Vấp", newlyInserted.getName());
        assertEquals("49 Quang Trung, 10, Gò Vấp, Hồ Chí Minh, Vietnam", newlyInserted.getAddress());
        assertEquals(10.8272542, newlyInserted.getLat());
        assertEquals(106.6071959, newlyInserted.getLon());
    }

    @Test
    public void atmNearbyShouldBeFoundWithIn5KM() {
        createAtmListNearbySearch();

        // at Nguyen Oanh Go Vap
        double currentLat = 10.8470016;
        double currentLon = 106.6743678;

        // approximately 5 km center, only 2 atms
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("", currentLat, currentLon, 5000);
        assertEquals(2, nearbyAtms.size());
        assertEquals(Long.valueOf(1l), nearbyAtms.get(0).getId());
        assertEquals(Long.valueOf(3l), nearbyAtms.get(1).getId());
    }

    @Test
    public void atmNearbyShouldBeFoundWithIn10km() {
        createAtmListNearbySearch();

        // at Nguyen Oanh Go Vap
        double currentLat = 10.8470016;
        double currentLon = 106.6743678;

        // approximately 5 km center
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("", currentLat, currentLon, 10000);
        assertEquals(3, nearbyAtms.size());
        assertEquals(Long.valueOf(1l), nearbyAtms.get(0).getId());
        assertEquals(Long.valueOf(3l), nearbyAtms.get(1).getId());
        assertEquals(Long.valueOf(2l), nearbyAtms.get(2).getId());
    }

    @Test
    public void atmNearbyWithNameContainsVietcombankShouldBeFoundWithIn5km() {
        createAtmListNearbySearch();

        // at Nguyen Oanh Go Vap
        double currentLat = 10.8470016;
        double currentLon = 106.6743678;

        // approximately 5 km center
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("Vietcombank", currentLat, currentLon, 5000);
        assertEquals(1, nearbyAtms.size());
        assertEquals(Long.valueOf(1l), nearbyAtms.get(0).getId());
    }

    private void createAtmListNearbySearch() {
        Atm atm = createEntity(1l);
        atm.setName("Vietcombank chi nhánh Quang Trung Gò Vấp");
        atm.setAddress("49 Quang Trung, 10, Gò Vấp, Hồ Chí Minh, Vietnam");
        atm.setLat(10.8389326);
        atm.setLon(106.661651);

        dao.insert(atm);

        atm = createEntity(2l);
        atm.setName("Citibank Võ Văn Tần");
        atm.setAddress("34-34A, Võ Văn Tần, phường 6, Hồ Chí Minh, Vietnam");
        atm.setLat(10.778656);
        atm.setLon(106.6221091);

        dao.insert(atm);

        atm = createEntity(3l);
        atm.setName("Dong A Bank Hà Huy Giáp");
        atm.setAddress("540 Hà Huy Giáp, Thạnh Lộc, 12, Hồ Chí Minh, Vietnam");
        atm.setLat(10.877963);
        atm.setLon(106.6776794);

        dao.insert(atm);
    }

    @NonNull
    private Atm createAtmForInsertion() {
        Atm atm = createEntity(1l);
        atm.setName("Vietcombank chi nhánh Quang Trung Gò Vấp");
        atm.setAddress("49 Quang Trung, 10, Gò Vấp, Hồ Chí Minh, Vietnam");
        atm.setLat(10.8272542);
        atm.setLon(106.6071959);
        return atm;
    }
}
