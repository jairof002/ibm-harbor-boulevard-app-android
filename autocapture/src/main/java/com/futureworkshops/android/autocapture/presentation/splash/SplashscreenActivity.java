package com.futureworkshops.android.autocapture.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.futureworkshops.android.autocapture.R;
import com.futureworkshops.android.autocapture.presentation.camera.view.CameraActivity;
import com.futureworkshops.datacap.common.utils.SnackUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SplashscreenActivity extends AppCompatActivity implements SplashscreenContract.View {

    @Inject
    SplashscreenContract.Presenter mPresenter;

    private TextView mStatusTv;

    /**
     * ALWAYS call AndroidInjection.inject(this) before ANYTHING else inside your Activity/Fragment.
     * {@link AndroidInjection} is the Dagger class responsible for injecting fields into Android Framework Components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mStatusTv = findViewById(R.id.status_tv);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // authenticate
        mPresenter.login();
    }

    @Override
    public void showIsAuthenticating() {
        mStatusTv.setText("Authenticating...");
    }

    @Override
    public void showDownloadingConfiguration() {
        mStatusTv.setText("Downloading configuration...");
    }

    @Override
    public void showCreatingBatch() {
        mStatusTv.setText("Creating batch...");
    }

    @Override
    public void onBatchCreated() {
        // go to next screen
        startActivity(new Intent(this, CameraActivity.class));
    }

    @Override
    public void onLoginSuccess() {
        /*
        Normally, you would need to download a list of available configurations
       and pick the name of the configuration you want to download.

        DatacapApi.getDcoList(appname) -> this will return a List<IDcoItem>

        You need to pick a IDcoItem based on your specific business rules and
        the use it's name to download the actual DCO (referred to as BatchConfiguration)

        IDcoItem.getname()


        For this sample app, we know the DCO name is the same as
        the datacap application name so we skip this step and go straight to
        downloading the configuration.
         */

        mPresenter.getConfiguration();
    }

    @Override
    public void onLoginFailed(String message) {
        SnackUtils.showSimpleSnackbar(this,message);
    }

    @Override
    public void onDownloadDcoSuccess() {
        /*
            After we have the configuration, we need to create a Batch that
            can be used in the rest of the app.

            It is not required to create a Batch before using the Datacap SDK but it makes things
            easier for the current sample app.
         */
        mPresenter.createBatch();

    }

    @Override
    public void onDownloadDcoFailed(String message) {
        SnackUtils.showSimpleSnackbar(this,message);
    }


}
