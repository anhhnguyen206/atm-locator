package me.anhnguyen.atmfinder.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import rx.observers.TestSubscriber;
import static org.junit.Assert.assertEquals;

/**
 * Created by nguyenhoanganh on 10/27/15.
 */
public class FindAtmInteractorImplTest {
    AtmRepository atmRepository;
    FindAtmInteractorImpl findAtmInteractor;

    @Before
    public void setUp() {
        atmRepository = Mockito.mock(AtmRepository.class);
        findAtmInteractor = new FindAtmInteractorImpl(atmRepository);
    }

    @Test
    public void emitCorrectValues() throws Exception {
        Mockito.when(atmRepository.findNearbyAtms(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Arrays.asList(new Atm(1l, "Name", "AbcXyz", 10.58948594, 107.54454545)));
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        findAtmInteractor.execute("Somename", 10.69859859, 107.868958565, 5000).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

    @Test(expected = Exception.class)
    public void emitCorrectError() {
        Mockito.when(atmRepository.findNearbyAtms(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new Exception("Test"));
        TestSubscriber<List<Atm>> testSubscriber = new TestSubscriber<>();
        findAtmInteractor.execute("Somename", 10.69859859, 107.868958565, 5000).subscribe(testSubscriber);

        assertEquals("Test", testSubscriber.getOnErrorEvents().get(0));
    }
}