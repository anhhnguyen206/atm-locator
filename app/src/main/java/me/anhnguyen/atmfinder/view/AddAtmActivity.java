package me.anhnguyen.atmfinder.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import butterknife.Bind;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.common.LocationUtils;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Scheduler;

public class AddAtmActivity extends InjectableActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {
    private GoogleMap map;

    @Bind(R.id.edit_text_name)
    EditText nameEditText;
    @Bind(R.id.edit_text_address)
    EditText addressEditTetx;
    @Bind(R.id.save_fab)
    FloatingActionButton addAtmFab;

    @Inject
    ReactiveLocationProvider reactiveLocationProvider;
    @Inject
    @ForSchedulerIo
    Scheduler schedulerIo;
    @Inject
    @ForSchedulerUi
    Scheduler schedulerUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atm);

        showProgress(getString(R.string.loading));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(false);
        map.setOnCameraChangeListener(this);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        reactiveLocationProvider
                .getReverseGeocodeObservable(cameraPosition.target.latitude, cameraPosition.target.longitude, 1)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(addresses -> LocationUtils.addressAsString(addresses.get(0)));
    }
}
