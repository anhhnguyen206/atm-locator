package me.anhnguyen.atmfinder.dependency;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.dependency.annotation.ForActivity;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.view.atm.add.AddAtmActivity;
import me.anhnguyen.atmfinder.view.atm.finder.AtmFinderActivitiy;
import me.anhnguyen.atmfinder.view.base.InjectableActivity;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
@Module(
        injects = { AtmFinderActivitiy.class, AddAtmActivity.class },
        addsTo = ApplicationModule.class,
        includes = ViewModelModule.class,
        library = true,
        complete = false
)
public class ActivityModule {
    private InjectableActivity injectableActivity;

    public ActivityModule(InjectableActivity injectableActivity) {
        this.injectableActivity = injectableActivity;
    }

    @Provides
    @Singleton
    @ForActivity
    Context provideContext() {
        return injectableActivity;
    }

    @Provides
    @Singleton
    @ForSchedulerIo
    Scheduler provideSchedulerIo() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @ForSchedulerUi
    Scheduler provideSchedulerUi() {
        return AndroidSchedulers.mainThread();
    }
}
