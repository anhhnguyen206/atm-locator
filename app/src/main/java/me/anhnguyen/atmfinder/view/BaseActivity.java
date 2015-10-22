package me.anhnguyen.atmfinder.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.anhnguyen.atmfinder.R;
import rx.Observable;

/**
 * Created by nguyenhoanganh on 10/18/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    @Bind(R.id.toolbar)
    @Nullable
    Toolbar toolbar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (progressDialog == null) {
            initProgressDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
        setUpActionBar();
    }

    private void setUpActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void showProgress(int resId) {
        progressDialog.setMessage(getString(resId));
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    protected Observable<Permission> requestLocationPermission() {
        return RxPermissions.getInstance(this)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    protected boolean locationPermissionGranted() {
        return RxPermissions.getInstance(this).isGranted(Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
