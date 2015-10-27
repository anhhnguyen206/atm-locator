package me.anhnguyen.atmfinder.view.base;

import android.os.Bundle;

import me.anhnguyen.atmfinder.AtmFinderApplication;
import me.anhnguyen.atmfinder.dependency.ActivityComponent;
import me.anhnguyen.atmfinder.dependency.ActivityModule;
import me.anhnguyen.atmfinder.dependency.DaggerActivityComponent;
import me.anhnguyen.atmfinder.dependency.InteractorModule;
import me.anhnguyen.atmfinder.dependency.ViewModelModule;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public abstract class InjectableActivity extends BaseActivity {
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AtmFinderApplication application = (AtmFinderApplication) getApplication();

        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(application.component())
                .activityModule(new ActivityModule(this))
                .interactorModule(new InteractorModule())
                .viewModelModule(new ViewModelModule())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
