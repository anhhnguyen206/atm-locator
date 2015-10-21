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
    public void searchTextShouldBeEmptyStringByDefault() {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.searchText().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue("");
    }

    @Test
    public void searchRangeShouldBe2000ByDefault() {
        TestSubscriber<Double> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.searchRange().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Double.valueOf(2000));
    }

    @Test
    public void atmListShouldHaveAllAtmsWithMaxRangeNoKeywordSearch() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        atmFinderViewModel.searchLat().onNext(currentLat);
        atmFinderViewModel.searchLon().onNext(currentLon);
        atmFinderViewModel.searchRange().onNext((double) 20000);
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(3, testSubscriber.getOnNextEvents().get(1).size());
    }

    @Test
    public void atmListShouldHaveOneAtmWithMaxRangeAndVietcombankKeywordSearch() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        atmFinderViewModel.searchLat().onNext(currentLat);
        atmFinderViewModel.searchLon().onNext(currentLon);
        atmFinderViewModel.searchRange().onNext((double) 20000);
        atmFinderViewModel.searchText().onNext("Vietcombank");
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(1, testSubscriber.getOnNextEvents().get(1).size());
    }

    @Test
    public void atmListShouldHaveOneAtmWithMaxRangeAndNonExistingKeywordSearch() {
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.atms().subscribe(testSubscriber);
        atmFinderViewModel.searchLat().onNext(currentLat);
        atmFinderViewModel.searchLon().onNext(currentLon);
        atmFinderViewModel.searchRange().onNext((double) 20000);
        atmFinderViewModel.searchText().onNext("jghfkjhgf");
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        assertEquals(0, testSubscriber.getOnNextEvents().get(1).size());
    }

    @Test
    public void loadingShouldEmitCorrectValues() {
        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.loading().subscribe(testSubscriber);
        atmFinderViewModel.searchLat().onNext(currentLat);
        atmFinderViewModel.searchLon().onNext(currentLon);
        atmFinderViewModel.searchRange().onNext((double) 20000);
        atmFinderViewModel.searchText().onNext("Vietcombank");
        atmFinderViewModel.search();
        testSubscriber.assertNoErrors();
        // first emitted value is the default Boolean.False
        // second emitted value is Boolean.True -> to signal the view to enable progress
        // third emitted value is Boolean.False -> search has done.
        testSubscriber.assertValueCount(3);
        testSubscriber.assertValues(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
    }

    @Test(expected = Exception.class)
    public void errorShouldEmitCorrectValue() {
        atmRepository = Mockito.mock(AtmRepository.class);
        findAtmInteractor = new FindAtmInteractorImpl(atmRepository);
        atmFinderViewModel = new AtmFinderViewModelImpl(findAtmInteractor, Schedulers.immediate(), Schedulers.immediate());
        Mockito.when(atmRepository.findNearbyAtms(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new Exception("Test"));

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        atmFinderViewModel.error().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue("Test");
    }
}
