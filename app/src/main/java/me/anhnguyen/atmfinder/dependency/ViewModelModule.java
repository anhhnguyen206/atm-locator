package me.anhnguyen.atmfinder.dependency;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.viewmodel.atm.add.AddAtmViewModel;
import me.anhnguyen.atmfinder.viewmodel.atm.add.AddAtmViewModelImpl;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModel;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModelImpl;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
@Module
public class ViewModelModule {
    @Provides
    AtmFinderViewModel provideAtmFinderViewModel(AtmFinderViewModelImpl atmFinderViewModel) {
        return atmFinderViewModel;
    }

    @Provides
    AddAtmViewModel provideAddAtmViewModel(AddAtmViewModelImpl addAtmViewModel) {
        return addAtmViewModel;
    }
}
