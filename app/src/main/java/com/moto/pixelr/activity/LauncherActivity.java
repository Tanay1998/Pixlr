package com.moto.pixelr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.moto.pixelr.MainActivity;
import com.moto.pixelr.R;
import com.motorola.mod.ModManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class LauncherActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.launcher_button) Button retryButton;
    @BindView(R.id.launcher_progressBar) ProgressBar progressBar;
    @BindView(R.id.launcher_text) TextView launcherText;

    @OnClick(R.id.launcher_button)
    public void launcherButton() {
        isDeviceMotoModReady(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        unbinder = ButterKnife.bind(this);

        initView();
        isDeviceMotoModReady(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initView() {
        launcherText.setText("Checking device compatibility...");
        progressBar.setVisibility(View.VISIBLE);
    }

    public void isDeviceMotoModReady(Context context) {

        int service = ModManager.isModServicesAvailable(context);
        String deviceStatus;
        switch (service) {
            case ModManager.SUCCESS:
                launchActivity();
                return;
            case ModManager.SERVICE_MISSING:
                deviceStatus = context.getString(R.string.SERVICE_MISSING);
                break;
            case ModManager.SERVICE_UPDATING:
                deviceStatus = context.getString(R.string.SERVICE_UPDATING);
                break;
            case ModManager.SERVICE_VERSION_UPDATE_REQUIRED:
                deviceStatus = context.getString(R.string.SERVICE_VERSION_UPDATE_REQUIRED);
                break;
            case ModManager.SERVICE_DISABLED:
                deviceStatus = context.getString(R.string.SERVICE_DISABLED);
                break;
            case ModManager.SERVICE_INVALID:
                deviceStatus = context.getString(R.string.SERVICE_INVALID);
                break;
            default:
                deviceStatus = context.getString(R.string.SERVICE_NOT_AVAILABLE);
                break;
        }

        launcherText.setText(deviceStatus);
        progressBar.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }

    private void launchActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
