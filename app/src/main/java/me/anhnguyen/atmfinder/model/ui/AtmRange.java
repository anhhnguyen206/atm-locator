package me.anhnguyen.atmfinder.model.ui;

/**
 * Created by nguyenhoanganh on 10/21/15.
 */
public class AtmRange {
    private String name;
    private double range;

    public AtmRange(String name, double range) {
        this.name = name;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public double getRange() {
        return range;
    }
}
