package me.anhnguyen.atmfinder.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import butterknife.Bind;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.viewmodel.AtmFinderViewModel;

public class AtmFinderActivitiy extends InjectableActivity implements OnMapReadyCallback {
    private GoogleMap map;

    @Bind(R.id.progress)
    ProgressBar progressBar;

    @Inject
    AtmFinderViewModel atmFinderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_finder);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bindViewModel();
    }

    private void bindViewModel() {
        // bind search progress loading
        atmFinderViewModel.loading()
                .subscribe(loading -> showSearchProgress(loading));

        // bind error message
        atmFinderViewModel.error()
                .subscribe(error -> showToast(error));

    }

    private void showSearchProgress(Boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(map.getMyLocation().getLatitude(),
                map.getMyLocation().getLongitude())));
    }
}
