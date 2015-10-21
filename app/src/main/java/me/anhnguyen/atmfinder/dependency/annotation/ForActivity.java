package me.anhnguyen.atmfinder.dependency.annotation;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
@Qualifier
@Retention(RUNTIME)
public @interface ForActivity {
}
