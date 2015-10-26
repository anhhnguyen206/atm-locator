package me.anhnguyen.atmfinder.view.atm.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.ActivityEvent;

import javax.inject.Inject;

import butterknife.Bind;
import me.anhnguyen.atmfinder.Constants;
import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.common.LocationUtils;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerIo;
import me.anhnguyen.atmfinder.dependency.annotation.ForSchedulerUi;
import me.anhnguyen.atmfinder.view.base.LocationBasedActivitiy;
import me.anhnguyen.atmfinder.viewmodel.atm.add.AddAtmViewModel;
import rx.Scheduler;

public class AddAtmActivity extends LocationBasedActivitiy implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {
    private GoogleMap map;

    @Bind(R.id.edit_text_name)
    EditText nameEditText;
    @Bind(R.id.edit_text_address)
    EditText addressEditText;
    @Bind(R.id.save_fab)
    FloatingActionButton addAtmFab;

    @Inject
    AddAtmViewModel addAtmViewModel;
    @Inject
    @ForSchedulerIo
    Scheduler schedulerIo;
    @Inject
    @ForSchedulerUi
    Scheduler schedulerUi;

    public static Intent getActivityIntent(Context context, String keyword, LatLng centerLatLng) {
        Intent intent = new Intent(context, AddAtmActivity.class);
        intent.putExtra(Constants.Bundle.KEYWORD, keyword);
        intent.putExtra(Constants.Bundle.CENTER_LATLNG, centerLatLng);
        return intent;
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
        bindViewModel();
        prePopulateData();
    }

    private void prePopulateData() {
        LatLng centerLatLng = getIntent().getParcelableExtra(Constants.Bundle.CENTER_LATLNG);
        String keyword = getIntent().getStringExtra(Constants.Bundle.KEYWORD);

        if (centerLatLng != null) {
            addAtmViewModel.setLatLng(centerLatLng);
        }

        if (!TextUtils.isEmpty(keyword)) {
            addAtmViewModel.setName(keyword);
        }
    }

    private void bindViewModel() {
        RxTextView.textChanges(nameEditText)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(charSequence -> addAtmViewModel.setName(charSequence.toString()));

        RxTextView.textChanges(addressEditText)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(charSequence -> addAtmViewModel.setAddress(charSequence.toString()));

        addAtmViewModel.error()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(s -> showToast(s));

        addAtmViewModel.loading()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(loading -> {
                    if (loading) {
                        showProgress(getString(R.string.loading));
                    } else {
                        hideProgress();
                    }
                });

        addAtmViewModel.canSave()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(canSave -> addAtmFab.setClickable(canSave));

        RxView.clicks(addAtmFab)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> addAtmViewModel.add());

        addAtmViewModel.done()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(atm -> finish());

        addAtmViewModel.latLng()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(latLng -> {
                    map.setOnCameraChangeListener(null);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    map.animateCamera(cameraUpdate);
                    map.setOnCameraChangeListener(this);
                });
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        addAtmViewModel.setLatLng(cameraPosition.target);

        reverseGeocode(cameraPosition.target.latitude, cameraPosition.target.longitude, 1)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(schedulerIo)
                .observeOn(schedulerUi)
                .subscribe(address -> addressEditText.setText(LocationUtils.addressAsString(address)));
    }
}
