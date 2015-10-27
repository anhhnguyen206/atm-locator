package me.anhnguyen.atmfinder.dependency;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.dependency.annotation.ForApplication;
import me.anhnguyen.atmfinder.model.dao.AtmDao;
import me.anhnguyen.atmfinder.model.dao.DaoMaster;
import me.anhnguyen.atmfinder.model.dao.DaoSession;
import me.anhnguyen.atmfinder.repository.AtmRepository;
import me.anhnguyen.atmfinder.repository.AtmRepositoryImpl;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
@Module
public class ApplicationModule {
    private Context context;


    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "atm-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        return daoMaster.newSession();
    }

    @Provides
    @Singleton
    AtmDao provideAtmDao(DaoSession daoSession) {
        return daoSession.getAtmDao();
    }

    @Provides
    @Singleton
    AtmRepository provideAtmRepository(AtmRepositoryImpl atmRepository) {
        return atmRepository;
    }

    @Provides
    @Singleton
    ReactiveLocationProvider provideReactiveLocationProvider() {
        return new ReactiveLocationProvider(context);
    }
}
