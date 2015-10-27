package me.anhnguyen.atmfinder;

import android.app.Application;

import me.anhnguyen.atmfinder.dependency.ApplicationComponent;
import me.anhnguyen.atmfinder.dependency.ApplicationModule;
import me.anhnguyen.atmfinder.dependency.DaggerApplicationComponent;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AtmFinderApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent component() {
        return applicationComponent;
    }
}
