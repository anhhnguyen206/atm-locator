package me.anhnguyen.atmfinder.dependency;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.interactor.AddAtmInteractor;
import me.anhnguyen.atmfinder.interactor.AddAtmInteractorImpl;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractor;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractorImpl;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
@Module
public class InteractorModule {
    @Provides
    AddAtmInteractor provideAddAtmInteractor(AddAtmInteractorImpl addAtmInteractor) {
        return addAtmInteractor;
    }

    @Provides
    FindAtmInteractor provideFindAtmInteractor(FindAtmInteractorImpl findAtmInteractor) {
        return findAtmInteractor;
    }
}
