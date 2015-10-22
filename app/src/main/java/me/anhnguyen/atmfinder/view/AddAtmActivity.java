package me.anhnguyen.atmfinder.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import butterknife.Bind;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.common.LocationUtils;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.viewmodel.AddAtmViewModel;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Scheduler;

public class AddAtmActivity extends InjectableActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {
    private GoogleMap map;

    @Bind(R.id.edit_text_name)
    EditText nameEditText;
    @Bind(R.id.edit_text_address)
    EditText addressEditText;
    @Bind(R.id.save_fab)
    FloatingActionButton addAtmFab;
    @Bind(R.id.pin)
    ImageView pinImageView;

    @Inject
    AddAtmViewModel addAtmViewModel;
    @Inject
    ReactiveLocationProvider reactiveLocationProvider;
    @Inject
    @ForSchedulerIo
    Scheduler schedulerIo;
    @Inject
    @ForSchedulerUi
    Scheduler schedulerUi;

    public static Intent getActivityIntent(Context context) {
        return new Intent(context, AddAtmActivity.class);
    }

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
        hideProgress();
        map = googleMap;
        map.setMyLocationEnabled(false);
        map.setOnCameraChangeListener(this);

        if (RxPermissions.getInstance(this).isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            reactiveLocationProvider.getUpdatedLocation(LocationUtils.currentLocationRequest())
                    .subscribeOn(schedulerIo)
                    .observeOn(schedulerUi)
                    .subscribe(location -> {
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                    });
        }

        bindViewModel();
    }

    private void bindViewModel() {
        RxTextView.textChanges(nameEditText)
                .subscribe(charSequence -> addAtmViewModel.setName(charSequence.toString()));

        RxTextView.textChanges(addressEditText)
                .subscribe(charSequence -> addAtmViewModel.setAddress(charSequence.toString()));

        addAtmViewModel.error()
                .subscribe(s -> showToast(s));

        addAtmViewModel.loading()
                .subscribe(loading -> {
                    if (loading) {
                        showProgress(getString(R.string.loading));
                    } else {
                        hideProgress();
                    }
                });

        addAtmViewModel.canSave()
                .subscribe(canSave -> addAtmFab.setClickable(canSave));

        RxView.clicks(addAtmFab)
                .subscribe(o -> addAtmViewModel.add());

        addAtmViewModel.done()
                .subscribe(atm -> finish());
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        addAtmViewModel.setLat(cameraPosition.target.latitude);
        addAtmViewModel.setLon(cameraPosition.target.longitude);

        reactiveLocationProvider
                .getReverseGeocodeObservable(cameraPosition.target.latitude, cameraPosition.target.longitude, 1)
                .filter(addresses -> addresses.size() > 0)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(addresses -> addressEditText.setText(LocationUtils.addressAsString(addresses.get(0))));
    }
}
