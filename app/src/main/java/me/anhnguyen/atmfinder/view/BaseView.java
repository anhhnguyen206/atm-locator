package me.anhnguyen.atmfinder.view;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public interface BaseView {
    void showProgress(String message);
    void showProgress(int resId);
    void hideProgress();
    void showToast(String message);
    void showToast(int resId);
}
