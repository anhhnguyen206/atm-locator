package me.anhnguyen.atmfinder.interactor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;
import me.anhnguyen.atmfinder.BuildConfig;
import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.model.dao.AtmDao;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import me.anhnguyen.atmfinder.repository.AtmRepositoryImpl;
import rx.observers.TestSubscriber;

/**
 * Created by nguyenhoanganh on 10/22/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddAtmInteractorImplTest extends AbstractDaoTestLongPk<AtmDao, Atm> {
    private AtmRepository atmRepository;
    private AddAtmInteractorImpl addAtmInteractor;

    public AddAtmInteractorImplTest() {
        super(AtmDao.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        atmRepository = new AtmRepositoryImpl(dao);
        addAtmInteractor = new AddAtmInteractorImpl(atmRepository);
    }

    @Override
    protected Atm createEntity(Long key) {
        return new Atm(key);
    }

    @Test
    public void shouldReturnAtmWhenAdd() {
        TestSubscriber<Atm> atmTestSubscriber = new TestSubscriber<>();
        addAtmInteractor.execute("Vcb", "Nguyen Oanh", 10.67584, 106.58585)
                .subscribe(atmTestSubscriber);

        atmTestSubscriber.assertNoErrors();
        atmTestSubscriber.assertValueCount(1);
        Atm atm = atmTestSubscriber.getOnNextEvents().get(0);
        assertEquals(Long.valueOf(1), atm.getId());
        assertEquals("Vcb", atm.getName());
        assertEquals("Nguyen Oanh", atm.getAddress());
        assertEquals(10.67584, atm.getLat());
        assertEquals(106.58585, atm.getLon());
    }

    @Test
    public void shouldCallOnErrorWhenNameIsEmpty() {
        TestSubscriber<Atm> atmTestSubscriber = new TestSubscriber<>();
        addAtmInteractor.execute("", "Nguyen Oanh", 10.67584, 106.58585)
                .subscribe(atmTestSubscriber);

        atmTestSubscriber.assertError(Exception.class);
        Throwable result = atmTestSubscriber.getOnErrorEvents().get(0);
        assertEquals("Name cannot be empty", result.getMessage());
    }

    @Test
    public void shouldCallOnErrorWhenAddressIsEmpty() {
        TestSubscriber<Atm> atmTestSubscriber = new TestSubscriber<>();
        addAtmInteractor.execute("Vcb", "", 10.67584, 106.58585)
                .subscribe(atmTestSubscriber);

        atmTestSubscriber.assertError(Exception.class);
        Throwable result = atmTestSubscriber.getOnErrorEvents().get(0);
        assertEquals("Address cannot be empty", result.getMessage());
    }
}