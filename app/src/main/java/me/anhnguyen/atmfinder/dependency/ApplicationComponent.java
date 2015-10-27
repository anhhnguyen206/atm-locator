package me.anhnguyen.atmfinder.dependency;

import javax.inject.Singleton;

import dagger.Component;
import me.anhnguyen.atmfinder.AtmFinderApplication;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

/**
 * Created by nguyenhoanganh on 10/27/15.
 */
@Singleton
@Component(
        modules = {
            ApplicationModule.class
        }
)
public interface ApplicationComponent {
    void inject(AtmFinderApplication atmFinderApplication);
    AtmRepository getAtmRepository();
    ReactiveLocationProvider getReactiveLocationProvider();
}
