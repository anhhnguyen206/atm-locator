package me.anhnguyen.atmfinder.view;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.viewmodel.AtmFinderViewModel;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Scheduler;

public class AtmFinderActivitiy extends InjectableActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {
    private GoogleMap map;

    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.image_search_icon)
    ImageView searchImageView;
    @Bind(R.id.edit_text_search_text)
    EditText searchEditText;
    @Bind(R.id.spinner_range)
    Spinner rangeSpinner;
    AtmRangeAdapter atmRangeAdapter;
    @Bind(R.id.add_atm_fab)
    FloatingActionButton addAtmFab;

    @Inject
    AtmFinderViewModel atmFinderViewModel;
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
        setContentView(R.layout.activity_atm_finder);

        RxPermissions.getInstance(this)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(permission -> init());
    }

    private void init() {
        showProgress(getString(R.string.loading));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        atmRangeAdapter = new AtmRangeAdapter(this);
        rangeSpinner.setAdapter(atmRangeAdapter);
    }

    private void bindViewModel() {
        // bind search progress loading
        atmFinderViewModel.loading()
                .subscribe(loading -> showSearchProgress(loading));

        // bind error message
        atmFinderViewModel.error()
                .subscribe(error -> showToast(error));

        // bind result of atms
        atmFinderViewModel.atms()
                .subscribe(atms -> showAtmsAsMarkers(atms));

        // bind search text
        RxTextView.textChanges(searchEditText)
                .subscribe(charSequence -> atmFinderViewModel.searchText().onNext(charSequence.toString()));

        rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atmFinderViewModel.searchRange().onNext(atmRangeAdapter.getItem(position).getRange());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // bind click event to perform search
        RxView.clicks(searchImageView)
                .subscribe(o -> atmFinderViewModel.search());
    }

    private void showAtmsAsMarkers(List<Atm> atms) {
        map.clear();
        Observable.from(atms).subscribe(atm -> addAtmToMapAsMarker(atm));
    }

    private void addAtmToMapAsMarker(Atm atm) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.flat(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_atm_16dp));
        map.addMarker(markerOptions);
    }

    private void showSearchProgress(Boolean loading) {
        searchImageView.setVisibility(loading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        hideProgress();

        map = googleMap;

        bindViewModel();

        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setOnCameraChangeListener(this);

        if (RxPermissions.getInstance(this).isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setNumUpdates(1)
                    .setInterval(100);

            reactiveLocationProvider.getUpdatedLocation(request)
                    .subscribeOn(schedulerIo)
                    .observeOn(schedulerUi)
                    .subscribe(location -> {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                    });
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        atmFinderViewModel.searchLat().onNext(cameraPosition.target.latitude);
        atmFinderViewModel.searchLon().onNext(cameraPosition.target.longitude);
    }
}
