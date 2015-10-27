package me.anhnguyen.atmfinder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
public class Constants {
    public final static double EARTH_RADIUS = 6371000;

    public final static List<Double> RANGES = Arrays.asList((double) 2000, (double) 5000, (double) 10000, (double) 15000, (double) 20000);

    public static class Bundle {
        public static final String KEYWORD = "KEYWORD";
        public static final String CENTER_LATLNG = "CENTER_LATLNG";
    }
}
