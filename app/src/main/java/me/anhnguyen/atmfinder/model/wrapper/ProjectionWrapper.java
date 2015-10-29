package me.anhnguyen.atmfinder.model.wrapper;

import android.graphics.Point;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nguyenhoanganh on 10/29/15.
 */
public class ProjectionWrapper {
    private Projection projection;

    public ProjectionWrapper() {}

    public ProjectionWrapper(Projection projection) {
        this.projection = projection;
    }

    public Point toScreenLocation(LatLng latLng) {
        return projection.toScreenLocation(latLng);
    }
}
