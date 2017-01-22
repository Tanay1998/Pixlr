package com.moto.pixelr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.moto.pixelr.MainActivity;
import com.moto.pixelr.R;
import com.motorola.mod.ModManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class LauncherActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.launcher_text) TextView launcherText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        unbinder = ButterKnife.bind(this);

        checkMotoModsCompatibility();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void checkMotoModsCompatibility() {

        // TODO: Disable this if testing on a non-Motorola device.
        if (ModManager.isModServicesAvailable(LauncherActivity.this) == ModManager.SUCCESS) {
            launchActivity();
        } else {
            launcherText.setText("This device does not support Moto Mods.");
        }
    }

    private void launchActivity() {
        Intent intent = new Intent(this, PixelrActivity.class);
        startActivity(intent);
    }
}
