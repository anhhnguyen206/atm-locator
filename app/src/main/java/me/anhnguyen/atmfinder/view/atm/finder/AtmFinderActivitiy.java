package me.anhnguyen.atmfinder.view.atm.finder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.common.LocationUtils;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.model.dao.Atm;
import me.anhnguyen.atmfinder.view.atm.add.AddAtmActivity;
import me.anhnguyen.atmfinder.view.base.LocationBasedActivitiy;
import me.anhnguyen.atmfinder.viewmodel.atm.finder.AtmFinderViewModel;
import rx.Scheduler;

public class AtmFinderActivitiy extends LocationBasedActivitiy implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private GoogleMap map;
    private LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();

    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.image_search_icon)
    ImageView searchImageView;
    @Bind(R.id.edit_text_search_text)
    EditText searchEditText;
    @Bind(R.id.edit_text_address)
    TextView editTextAddress;
    @Bind(R.id.spinner_range)
    Spinner rangeSpinner;
    AtmRangeAdapter atmRangeAdapter;
    @Bind(R.id.add_atm_fab)
    FloatingActionButton addAtmFab;
    @Bind(R.id.my_location_fab)
    FloatingActionButton myLocationFab;

    @Inject
    AtmFinderViewModel atmFinderViewModel;
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
        init();
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
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(loading -> showSearchProgress(loading));

        // bind error message
        atmFinderViewModel.error()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(error -> showToast(error));

        // bind result of atms
        atmFinderViewModel.atms()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(atms -> atms.size() > 0)
                .subscribe(atms -> showAtmsAsMarkers(atms));

        // bind info message
        atmFinderViewModel.infoResId()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(resId -> showToast(resId));

        // bind search text
        RxTextView.textChanges(searchEditText)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(charSequence -> atmFinderViewModel.setKeyword(charSequence.toString()));

        rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atmFinderViewModel.setRange(atmRangeAdapter.getItem(position).getRange());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // bind click event to perform search
        RxView.clicks(searchImageView)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> atmFinderViewModel.search());

        RxView.clicks(addAtmFab)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> {
                    LatLng latLng = new LatLng(atmFinderViewModel.getLat(), atmFinderViewModel.getLon());
                    startActivity(AddAtmActivity.getActivityIntent(this, atmFinderViewModel.getKeyword(), latLng));
                });

        RxView.clicks(myLocationFab)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> getCurrentLocationAndMoveMap());

    }

    private void showAtmsAsMarkers(List<Atm> atms) {
        map.clear();

        for (Atm atm : atms) {
            Marker marker = addAtmToMapAsMarker(atm);
            latLngBoundsBuilder.include(marker.getPosition());
        }

        LatLngBounds bounds = latLngBoundsBuilder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
        map.animateCamera(cu);
    }

    private Marker addAtmToMapAsMarker(Atm atm) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.flat(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
        markerOptions.position(new LatLng(atm.getLat(), atm.getLon()));
        markerOptions.title(atm.getName());
        markerOptions.snippet(atm.getAddress());
        return map.addMarker(markerOptions);
    }

    private void showSearchProgress(Boolean loading) {
        searchImageView.setVisibility(loading ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        hideProgress();
        map = googleMap;
        map.setOnCameraChangeListener(this);
        map.getUiSettings().setMapToolbarEnabled(false);
        bindViewModel();
        getCurrentLocationAndMoveMap();
    }

    private void getCurrentLocationAndMoveMap() {
        if (locationPermissionGranted()) {
            currentLocation()
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(schedulerIo)
                    .observeOn(schedulerUi)
                    .subscribe(location -> {
                        atmFinderViewModel.setLat(location.getLatitude());
                        atmFinderViewModel.setLat(location.getLongitude());
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                    });

        } else {
            requestLocationPermission()
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(permission -> {
                        if (permission.granted) {
                            getCurrentLocationAndMoveMap();
                        } else {
                            showToast(getString(R.string.location_permission_not_granted));
                            LatLng latLng = LocationUtils.defaultLatLng();
                            atmFinderViewModel.setLat(latLng.latitude);
                            atmFinderViewModel.setLon(latLng.longitude);
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        }
                    });
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        atmFinderViewModel.setLat(cameraPosition.target.latitude);
        atmFinderViewModel.setLon(cameraPosition.target.longitude);

        reverseGeocode(cameraPosition.target.latitude, cameraPosition.target.longitude, 1)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(address -> {
                    editTextAddress.setText(LocationUtils.addressAsString(address));
                });
    }
}
