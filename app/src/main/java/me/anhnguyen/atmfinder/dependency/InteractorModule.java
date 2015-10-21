package me.anhnguyen.atmfinder.dependency;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.interactor.AddAtmInteractor;
import me.anhnguyen.atmfinder.interactor.AddAtmInteractorImpl;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractor;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractorImpl;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
@Module(
        library = true,
        complete = false
)
public class InteractorModule {
    @Provides
    @Singleton
    AddAtmInteractor provideAddAtmInteractor(AddAtmInteractorImpl addAtmInteractor) {
        return addAtmInteractor;
    }

    @Provides
    @Singleton
    FindAtmInteractor provideFindAtmInteractor(FindAtmInteractorImpl findAtmInteractor) {
        return findAtmInteractor;
    }
}
