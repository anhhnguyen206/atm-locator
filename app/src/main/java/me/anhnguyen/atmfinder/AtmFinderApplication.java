package me.anhnguyen.atmfinder;

import android.app.Application;

import dagger.ObjectGraph;
import me.anhnguyen.atmfinder.dependency.ApplicationModule;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public class AtmFinderApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new ApplicationModule(this));
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public ObjectGraph getApplicationGraph() {
        return objectGraph;
    }
}
