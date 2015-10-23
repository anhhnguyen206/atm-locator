package me.anhnguyen.atmfinder.view.base;

import android.Manifest;
import android.content.IntentSender;
import android.location.Address;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import me.anhnguyen.atmfinder.R;
import me.anhnguyen.atmfinder.common.LocationUtils;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/23/15.
 */
public abstract class LocationBasedActivitiy extends InjectableActivity {
    private static final int REQUEST_CHECK_SETTINGS = 1;

    @Inject
    ReactiveLocationProvider reactiveLocationProvider;

    protected Observable<Permission> requestLocationPermission() {
        return RxPermissions.getInstance(this)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    protected boolean locationPermissionGranted() {
        return RxPermissions.getInstance(this).isGranted(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    protected Observable<Address> reverseGeocode(double lat, double lon, int limit) {
        return reactiveLocationProvider
                .getReverseGeocodeObservable(lat, lon, 1)
                .filter(addresses -> addresses.size() > 0)
                .map(addresses -> addresses.get(0));
    }

    protected Observable<Location> currentLocation() {
        LocationRequest locationRequest = LocationUtils.currentLocationRequest();

        LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                        .setAlwaysShow(true)
                        .build();

        return reactiveLocationProvider.checkLocationSettings(locationSettingsRequest)
                .doOnNext(locationSettingsResult -> resolveLocationSettingsResult(locationSettingsResult))
                .flatMap(locationSettingsResult -> {
                    if (locationSettingsResult.getStatus().isCanceled()) {
                        return Observable.just(LocationUtils.defaultLocation());
                    } else {
                        return reactiveLocationProvider.getUpdatedLocation(LocationUtils.currentLocationRequest());
                    }
                });
    }

    private void resolveLocationSettingsResult(LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();
        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
            try {
                status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                Log.e("AtmFinderActivity", "Error opening settings activity.", e);
            }
        }

        if (status.getStatus().isCanceled()) {
            showToast(getString(R.string.location_not_enabled));
        }
    }
}
