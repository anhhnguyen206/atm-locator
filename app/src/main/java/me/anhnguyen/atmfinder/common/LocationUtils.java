package me.anhnguyen.atmfinder.common;

import android.location.Address;

import com.google.android.gms.location.LocationRequest;

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

        return addressString;
    }

    public static LocationRequest currentLocationRequest() {
        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);
        return request;
    }
}
