package me.anhnguyen.atmfinder.dependency;

import dagger.Component;
import me.anhnguyen.atmfinder.dependency.annotation.ActivityScope;
import me.anhnguyen.atmfinder.interactor.AddAtmInteractor;
import me.anhnguyen.atmfinder.interactor.FindAtmInteractor;
import me.anhnguyen.atmfinder.view.atm.add.AddAtmActivity;
import me.anhnguyen.atmfinder.view.atm.finder.AtmFinderActivitiy;
import me.anhnguyen.atmfinder.viewmodel.atm.add.AddAtmViewModel;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModel;

/**
 * Created by nguyenhoanganh on 10/27/15.
 */
@ActivityScope
@Component(
        dependencies = ApplicationComponent.class,
        modules = { ActivityModule.class, InteractorModule.class, ViewModelModule.class }
)
public interface ActivityComponent {
    void inject(AtmFinderActivitiy activity);
    void inject(AddAtmActivity activity);
    AtmFinderViewModel getAtmFinderViewModel();
    AddAtmViewModel getAddAtmViewModel();
    FindAtmInteractor getFindAtmInteractor();
    AddAtmInteractor getAddAtmInteractor();
}
