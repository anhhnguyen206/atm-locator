package me.anhnguyen.atmfinder.dependency;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.viewmodel.atm.add.AddAtmViewModel;
import me.anhnguyen.atmfinder.viewmodel.atm.add.AddAtmViewModelImpl;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModel;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModelImpl;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
@Module(
        includes = InteractorModule.class,
        library = true,
        complete = false
)
public class ViewModelModule {
    @Provides
    @Singleton
    AtmFinderViewModel provideAtmFinderViewModel(AtmFinderViewModelImpl atmFinderViewModel) {
        return atmFinderViewModel;
    }

    @Provides
    @Singleton
    AddAtmViewModel provideAddAtmViewModel(AddAtmViewModelImpl addAtmViewModel) {
        return addAtmViewModel;
    }
}
