package me.anhnguyen.atmfinder.view;

import android.os.Bundle;

import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;
import me.anhnguyen.atmfinder.AtmFinderApplication;
import me.anhnguyen.atmfinder.dependency.ActivityModule;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public abstract class InjectableActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the activity graph by .plus-ing our modules onto the application graph.
        AtmFinderApplication application = (AtmFinderApplication) getApplication();
        ObjectGraph activityGraph = application.getApplicationGraph().plus(getModules().toArray());

        // Inject ourselves so subclasses will have dependencies fulfilled when this method returns.
        activityGraph.inject(this);
    }

    /**
     * A list of modules to use for the individual activity graph. Subclasses can override this
     * method to provide additional modules provided they call and include the modules returned by
     * calling {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Collections.singletonList(new ActivityModule(this));
    }
}
