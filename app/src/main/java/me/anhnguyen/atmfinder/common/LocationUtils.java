package me.anhnguyen.atmfinder.common;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nguyenhoanganh on 10/22/15.
 */
public class LocationUtils {
    public static String addressAsString(Address address) {
        String addressString = "";
        if (address.getMaxAddressLineIndex() > 0) {
            addressString += address.getAddressLine(0) != null && address.getAddressLine(0).length() > 0 ?
                    address.getAddressLine(0) + ", " : "";
        }

        if (address.getSubLocality() != null && address.getSubLocality().length() > 0) {
            addressString += address.getSubLocality() + ", ";
        }

        if (address.getSubAdminArea() != null && address.getSubAdminArea().length() > 0) {
            addressString += address.getSubAdminArea() + ", ";
        }

        if (address.getAdminArea() != null && address.getAdminArea().length() > 0) {
            addressString += address.getAdminArea() + ", ";
        }

        if (address.getCountryName() != null && address.getCountryName().length() > 0) {
            addressString += address.getCountryName();
        }

        if (addressString.endsWith(", ")) {
            addressString = addressString.substring(0, addressString.length() - 2);
        }

        return addressString;
    }

    public static LocationRequest currentLocationRequest() {
        return LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);
    }

    public static Location defaultLocation() {
        Location location = new Location("");
        location.setLatitude(10.7725563);
        location.setLongitude(106.6958022);
        return location;
    }

    // return default lat lng in case
    // we don't have location
    public static LatLng defaultLatLng() {
        return new LatLng(10.7725563, 106.6958022);
    }
}
