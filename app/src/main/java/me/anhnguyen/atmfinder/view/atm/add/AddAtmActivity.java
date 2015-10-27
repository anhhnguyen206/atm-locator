package me.anhnguyen.atmfinder.view.atm.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding.view.RxMenuItem;
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

        getActivityComponent().inject(this);

        showProgress(getString(R.string.loading));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_atm, menu);

        MenuItem addMenu = menu.findItem(R.id.action_add);

        RxMenuItem.clicks(addMenu)
                .subscribe(aVoid -> addAtmViewModel.add());

        addAtmViewModel.canSave()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(RxMenuItem.enabled(addMenu));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                addAtmViewModel.add();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(centerLatLng, 14);
            map.animateCamera(cameraUpdate);
        }

        if (!TextUtils.isEmpty(keyword)) {
            nameEditText.setText(keyword);
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

        addAtmViewModel.done()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(atm -> finish());

        addAtmViewModel.latLng()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .flatMap(latLng -> reverseGeocode(latLng.latitude, latLng.longitude, 1))
                .subscribe(address -> addressEditText.setText(LocationUtils.addressAsString(address)));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        addAtmViewModel.setLatLng(cameraPosition.target);
    }
}
