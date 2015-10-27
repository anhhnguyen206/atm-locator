package me.anhnguyen.atmfinder.repository;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;
import me.anhnguyen.atmfinder.AtmRepositoryTestHelper;
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
        createAtmListNearbySearch();
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

        Atm newlyInserted = atmRepository.insert(atm);

        assertNotNull(dao.load(newlyInserted.getId()));
        assertEquals("Vietcombank chi nhánh Quang Trung Gò Vấp", newlyInserted.getName());
        assertEquals("49 Quang Trung, 10, Gò Vấp, Hồ Chí Minh, Vietnam", newlyInserted.getAddress());
        assertEquals(10.8272542, newlyInserted.getLat());
        assertEquals(106.6071959, newlyInserted.getLon());
    }

    @Test
    public void atmNearbyShouldBeFoundWithIn5KM() {
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
        // at Nguyen Oanh Go Vap
        double currentLat = 10.8470016;
        double currentLon = 106.6743678;

        // approximately 10 km center
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("", currentLat, currentLon, 10000);
        assertEquals(4, nearbyAtms.size());
        assertEquals(Long.valueOf(1l), nearbyAtms.get(0).getId());
        assertEquals(Long.valueOf(3l), nearbyAtms.get(1).getId());
        assertEquals(Long.valueOf(4l), nearbyAtms.get(2).getId());
        assertEquals(Long.valueOf(2l), nearbyAtms.get(3).getId());
    }

    @Test
    public void atmNearbyWithNameContainsVietcombankShouldBeFoundWithIn5km() {
        // at Nguyen Oanh Go Vap
        double currentLat = 10.8470016;
        double currentLon = 106.6743678;

        // approximately 5 km center
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("Vietcombank", currentLat, currentLon, 5000);
        assertEquals(1, nearbyAtms.size());
        assertEquals(Long.valueOf(1l), nearbyAtms.get(0).getId());
    }

    @Test
    public void atmNearbyWithNameThatNotExistShouldNotBeFound() {
        // at Nguyen Oanh Go Vap
        double currentLat = 10.8470016;
        double currentLon = 106.6743678;

        // approximately 5 km center
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("NonExisting ATM", currentLat, currentLon, 5000);
        assertEquals(0, nearbyAtms.size());
    }

    @Test
    public void atmNearbyThatIsNotActuallyInTheSearchRangeShouldNotBeFound() {
        // at 147 Ton Dat Tien
        double currentLat = 10.723472955145452;
        double currentLon = 106.69874332845211;

        // approximately 5 km center
        List<Atm> nearbyAtms = atmRepository.findNearbyAtms("", currentLat, currentLon, 5000);
        assertEquals(0, nearbyAtms.size());
    }

    private void createAtmListNearbySearch() {
        AtmRepositoryTestHelper atmRepositoryTestHelper = new AtmRepositoryTestHelper(dao);
        atmRepositoryTestHelper.createAtmListNearbySearch();
    }

    @NonNull
    private Atm createAtmForInsertion() {
        Atm atm = new Atm();
        atm.setName("Vietcombank chi nhánh Quang Trung Gò Vấp");
        atm.setAddress("49 Quang Trung, 10, Gò Vấp, Hồ Chí Minh, Vietnam");
        atm.setLat(10.8272542);
        atm.setLon(106.6071959);
        return atm;
    }
}
