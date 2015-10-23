package me.anhnguyen.atmfinder.viewmodel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;
import me.anhnguyen.atmfinder.AtmRepositoryTestHelper;
import me.anhnguyen.atmfinder.BuildConfig;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractor;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractorImpl;
import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.model.dao.AtmDao;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import me.anhnguyen.atmfinder.repository.AtmRepositoryImpl;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModelImpl;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AtmFinderViewModelImplTest extends AbstractDaoTestLongPk<AtmDao, Atm> {
    private AtmRepository atmRepository;
    private FindAtmInteractor findAtmInteractor;
    private AtmFinderViewModelImpl atmFinderViewModel;
    // at Nguyen Oanh Go Vap
    private double currentLat = 10.8470016;
    private double currentLon = 106.6743678;

    public AtmFinderViewModelImplTest() {
        super(AtmDao.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        atmRepository = new AtmRepositoryImpl(dao);
        findAtmInteractor = new FindAtmInteractorImpl(atmRepository);
        atmFinderViewModel = new AtmFinderViewModelImpl(findAtmInteractor, Schedulers.immediate(), Schedulers.immediate());

        AtmRepositoryTestHelper atmRepositoryTestHelper = new AtmRepositoryTestHelper(dao);
        atmRepositoryTestHelper.createAtmListNearbySearch();
    }

    @Override
    protected Atm createEntity(Long key) {
        return null;
    }

    @Test
    public void atmListShouldBeEmptyByDefault() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Collections.<Atm>emptyList());
    }

    @Test
    public void loadingShouldBeFalseByDefault() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.loading().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Boolean.FALSE);
    }

    @Test
    public void keywordShouldBeEmptyStringByDefault() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.keyword().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue("");
    }

    @Test
    public void searchRangeShouldBe2000ByDefault() {
        TestSubscriber<Double> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.range().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue((double) 2000);
    }

    @Test
    public void latShouldEmitCorrectValueWhenSetLat() {
        TestSubscriber<Double> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.lat().subscribe(testSubscriber);
        atmFinderViewModel.setLat(20000);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue((double) 20000);
    }

    @Test
    public void lonShouldEmitCorrectValueWhenSetLon() {
        TestSubscriber<Double> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.lon().subscribe(testSubscriber);
        atmFinderViewModel.setLon(20000);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue((double) 20000);
    }

    @Test
    public void keywordShouldEmitCorrectValueWhenSetKeyword() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.keyword().subscribe(testSubscriber);
        atmFinderViewModel.setKeyword("VCB");
        testSubscriber.assertNoErrors();
        testSubscriber.assertValues("", "VCB");
    }

    @Test
    public void rangeShouldEmitCorrectValueWhenSetRange() {
        TestSubscriber<Double> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.range().subscribe(testSubscriber);
        atmFinderViewModel.setRange(20000);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValues((double) 2000, (double) 20000);
    }

    @Test
    public void atmListShouldHaveAllAtmsWithMaxRangeNoKeywordSearch() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        atmFinderViewModel.setLat(currentLat);
        atmFinderViewModel.setLon(currentLon);
        atmFinderViewModel.setRange((double) 20000);
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(3, testSubscriber.getOnNextEvents().get(1).size());
    }

    @Test
    public void atmListShouldHaveOneAtmWithMaxRangeAndVietcombankKeywordSearch() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        atmFinderViewModel.setLat(currentLat);
        atmFinderViewModel.setLon(currentLon);
        atmFinderViewModel.setRange((double) 20000);
        atmFinderViewModel.setKeyword("Vietcombank");
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(1, testSubscriber.getOnNextEvents().get(1).size());
    }

    @Test
    public void atmListShouldHaveNoAtmWithMaxRangeAndNonExistingKeywordSearch() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        atmFinderViewModel.setLat(currentLat);
        atmFinderViewModel.setLon(currentLon);
        atmFinderViewModel.setRange((double) 20000);
        atmFinderViewModel.setKeyword("jghfkjhgf");
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(0, testSubscriber.getOnNextEvents().get(1).size());
    }

    @Test
    public void loadingShouldEmitCorrectValues() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.loading().subscribe(testSubscriber);
        atmFinderViewModel.setLat(currentLat);
        atmFinderViewModel.setLon(currentLon);
        atmFinderViewModel.setRange((double) 20000);
        atmFinderViewModel.setKeyword("Vietcombank");
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        // first emitted value is the default Boolean.False
        // second emitted value is Boolean.True -> to signal the view to enable progress
        // third emitted value is Boolean.False -> search has done.
        testSubscriber.assertValueCount(3);
        testSubscriber.assertValues(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
    }

    @Test(expected = Exception.class)
    public void errorShouldEmitCorrectValue() throws Exception {
        findAtmInteractor = Mockito.mock(FindAtmInteractor.class);
        atmFinderViewModel = new AtmFinderViewModelImpl(findAtmInteractor, Schedulers.immediate(), Schedulers.immediate());

        Mockito.when(findAtmInteractor.execute(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Observable.error(new Exception("Test")));

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.error().subscribe(testSubscriber);
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue("Test");
    }
}
