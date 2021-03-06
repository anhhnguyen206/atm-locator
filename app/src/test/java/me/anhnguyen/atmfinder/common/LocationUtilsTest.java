package me.anhnguyen.atmfinder.common;

import android.graphics.Point;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import me.anhnguyen.atmfinder.BuildConfig;
import me.anhnguyen.atmfinder.model.wrapper.ProjectionWrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by nguyenhoanganh on 10/22/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocationUtilsTest {
    @Test
    public void addressAsStringWithAllInformation() throws Exception {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubLocality, SubAdminArea, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithAddressLineMissing() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(0);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "SubLocality, SubAdminArea, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithAddressLineNull() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn(null);
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "SubLocality, SubAdminArea, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithAddressLineEmpty() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "SubLocality, SubAdminArea, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithSubLocalityEmpty() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubAdminArea, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithSubLocalityNull() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn(null);
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubAdminArea, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithSubAdminAreaEmpty() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubLocality, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithSubAdminAreaNull() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn(null);
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubLocality, AdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithAdminAreaEmpty() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("");
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubLocality, SubAdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithAdminAreaNull() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn(null);
        Mockito.when(address.getCountryName()).thenReturn("CountryName");

        String expected = "AddressLine, SubLocality, SubAdminArea, CountryName";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithCountryNameEmpty() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn("");

        String expected = "AddressLine, SubLocality, SubAdminArea, AdminArea";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void addressAsStringWithCountryNameNull() {
        Address address = Mockito.mock(Address.class);
        Mockito.when(address.getMaxAddressLineIndex()).thenReturn(1);
        Mockito.when(address.getAddressLine(0)).thenReturn("AddressLine");
        Mockito.when(address.getSubLocality()).thenReturn("SubLocality");
        Mockito.when(address.getSubAdminArea()).thenReturn("SubAdminArea");
        Mockito.when(address.getAdminArea()).thenReturn("AdminArea");
        Mockito.when(address.getCountryName()).thenReturn(null);

        String expected = "AddressLine, SubLocality, SubAdminArea, AdminArea";
        String actual = LocationUtils.addressAsString(address);
        assertEquals(expected, actual);
    }

    @Test
    public void currentLocationRequestShouldReturnCorrectRequest() throws Exception {
        LocationRequest locationRequest = LocationUtils.currentLocationRequest();
        assertEquals(LocationRequest.PRIORITY_HIGH_ACCURACY, locationRequest.getPriority());
        assertEquals(1, locationRequest.getNumUpdates());
        assertEquals(100, locationRequest.getInterval());
    }

    @Test
    public void defaultLatLngShouldReturnCorrectLatitudeLongitude() throws Exception {
        LatLng latLng = LocationUtils.defaultLatLng();
        assertEquals(Double.valueOf(10.7725563), Double.valueOf(latLng.latitude));
        assertEquals(Double.valueOf(106.6958022), Double.valueOf(latLng.longitude));
    }

    @Test
    public void defaultLocationShouldReturnCorrectLatitudeLongitude() throws Exception {
        Location location = LocationUtils.defaultLocation();
        assertEquals(Double.valueOf(10.7725563), Double.valueOf(location.getLatitude()));
        assertEquals(Double.valueOf(106.6958022), Double.valueOf(location.getLongitude()));
    }

    @Test
    public void convertCenterAndRadiusToBoundsShouldReturnLatLngBounds() {
        assertNotNull(LocationUtils.convertCenterAndRadiusToBounds(LocationUtils.defaultLatLng(), 5000));
    }

    @Test
    public void metersToPixelsShouldReturnValue() {
        ProjectionWrapper projectionWrapper = Mockito.mock(ProjectionWrapper.class);
        Mockito.when(projectionWrapper.toScreenLocation(Mockito.any()))
                .thenReturn(new Point(1, 1));
        assertNotNull(LocationUtils.metersToPixels(projectionWrapper, LocationUtils.defaultLatLng(), 5000));
    }

    @Test
    public void testLocationUtilsConstructor() {
        assertNotNull(new LocationUtils());
    }
}