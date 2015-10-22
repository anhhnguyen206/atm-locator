package me.anhnguyen.atmfinder.viewmodel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import me.anhnguyen.atmfinder.interactor.AddAtmInteractor;
import me.anhnguyen.atmfinder.model.dao.Atm;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by nguyenhoanganh on 10/22/15.
 */

public class AddAtmViewModelImplTest {
    private AddAtmInteractor addAtmInteractor;
    private AddAtmViewModelImpl addAtmViewModelImpl;

    public AddAtmViewModelImplTest() {
    }

    @Before
    public void setUp() throws Exception {
        addAtmInteractor = Mockito.mock(AddAtmInteractor.class);
        addAtmViewModelImpl = new AddAtmViewModelImpl(addAtmInteractor,
                Schedulers.immediate(),
                Schedulers.immediate());
    }

    @Test
    public void onlyAllowSaveIfAllInformationAreFilled() {
        TestSubscriber<Boolean> canSaveSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.canSave()
                .subscribe(canSaveSubscriber);
        addAtmViewModelImpl.setName("Vietcombank");
        addAtmViewModelImpl.setAddress("Vietcombank");
        addAtmViewModelImpl.setLat(10.68588);
        addAtmViewModelImpl.setLon(107.59394);
        canSaveSubscriber.assertNoErrors();
        canSaveSubscriber.assertValue(Boolean.TRUE);
    }

    @Test
    public void notAllowSaveIfInformationIsMissing() {
        TestSubscriber<Boolean> canSaveSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.canSave()
                .subscribe(canSaveSubscriber);
        addAtmViewModelImpl.setName("Vietcombank");
        canSaveSubscriber.assertNoErrors();
        canSaveSubscriber.assertValueCount(0);
    }

    @Test
    public void notAllowSaveIfNameOrAddressIsEmpty() {
        TestSubscriber<Boolean> canSaveSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.canSave()
                .subscribe(canSaveSubscriber);
        addAtmViewModelImpl.setName("");
        addAtmViewModelImpl.setAddress("");
        addAtmViewModelImpl.setLat(10.68588);
        addAtmViewModelImpl.setLon(107.59394);

        canSaveSubscriber.assertNoErrors();
        canSaveSubscriber.assertValueCount(1);
        canSaveSubscriber.assertValue(Boolean.FALSE);
    }

    @Test
    public void loadingShouldEmitCorrectValue() {
        Atm atm = Mockito.mock(Atm.class);
        Mockito.when(atm.getName()).thenReturn("Vietcombank");
        Mockito.when(atm.getAddress()).thenReturn("Vietcombank");
        Mockito.when(atm.getLat()).thenReturn(10.68588);
        Mockito.when(atm.getLon()).thenReturn(107.59394);
        Mockito.when(addAtmInteractor.execute(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(Observable.just(atm));

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.loading().subscribe(testSubscriber);
        addAtmViewModelImpl.setName("Vietcombank");
        addAtmViewModelImpl.setAddress("Vietcombank");
        addAtmViewModelImpl.setLat(10.68588);
        addAtmViewModelImpl.setLon(107.59394);
        addAtmViewModelImpl.add();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(Boolean.TRUE, Boolean.FALSE);
    }

    @Test(expected = Exception.class)
    public void loadingShouldEmitCorrectValueIfErrorHappens() {
        Mockito.when(addAtmInteractor.execute(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new Exception("Test"));

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.loading().subscribe(testSubscriber);
        addAtmViewModelImpl.setName("Vietcombank");
        addAtmViewModelImpl.setAddress("Vietcombank");
        addAtmViewModelImpl.setLat(10.68588);
        addAtmViewModelImpl.setLon(107.59394);
        addAtmViewModelImpl.add();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(Boolean.TRUE, Boolean.FALSE);
    }

    @Test(expected = Exception.class)
    public void errorShouldEmitCorrectValue() {
        Mockito.when(addAtmInteractor.execute(Mockito.anyString(), Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenThrow(new Exception("Test"));

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.error().subscribe(testSubscriber);
        addAtmViewModelImpl.setName("Vietcombank");
        addAtmViewModelImpl.setAddress("Vietcombank");
        addAtmViewModelImpl.setLat(10.68588);
        addAtmViewModelImpl.setLon(107.59394);
        addAtmViewModelImpl.add();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValues("Test");
    }

    @Test
    public void doneShouldEmitInsertedAtm() {
        Mockito.when(addAtmInteractor.execute("Vietcombank", "Vietcombank", 10.68588, 107.59394))
                .thenReturn(Observable.just(new Atm(1l, "Vietcombank", "Vietcombank", 10.68588, 107.59394)));

        TestSubscriber<Atm> testSubscriber = new TestSubscriber<>();
        addAtmViewModelImpl.done().subscribe(testSubscriber);
        addAtmViewModelImpl.setName("Vietcombank");
        addAtmViewModelImpl.setAddress("Vietcombank");
        addAtmViewModelImpl.setLat(10.68588);
        addAtmViewModelImpl.setLon(107.59394);
        addAtmViewModelImpl.add();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        Atm actual = testSubscriber.getOnNextEvents().get(0);
        assertEquals("Vietcombank", actual.getName());
        assertEquals("Vietcombank", actual.getAddress());
        assertEquals(Double.valueOf(10.68588), actual.getLat());
        assertEquals(Double.valueOf(107.59394), actual.getLon());
    }
}