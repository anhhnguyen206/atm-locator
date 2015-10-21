package me.anhnguyen.atmfinder.dependency;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.anhnguyen.atmfinder.dependency.annotation.ForApplication;
import me.anhnguyen.atmfinder.model.dao.AtmDao;
import me.anhnguyen.atmfinder.model.dao.DaoMaster;
import me.anhnguyen.atmfinder.model.dao.DaoSession;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
@Module(
        library = true
)
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
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    @Provides
    @Singleton
    AtmDao provideAtmDao(DaoSession daoSession) {
        return daoSession.getAtmDao();
    }
}
