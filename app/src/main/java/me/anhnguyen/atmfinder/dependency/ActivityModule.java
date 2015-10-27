package me.anhnguyen.atmfinder.dependency;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.dependency.annotation.ForActivity;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.view.base.InjectableActivity;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
@Module
public class ActivityModule {
    private InjectableActivity injectableActivity;

    public ActivityModule(InjectableActivity injectableActivity) {
        this.injectableActivity = injectableActivity;
    }

    @Provides
    @ForActivity
    Context provideContext() {
        return injectableActivity;
    }

    @Provides
    @ForSchedulerIo
    Scheduler provideSchedulerIo() {
        return Schedulers.io();
    }

    @Provides
    @ForSchedulerUi
    Scheduler provideSchedulerUi() {
        return AndroidSchedulers.mainThread();
    }
}
