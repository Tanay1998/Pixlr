package com.moto.pixelr.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moto.pixelr.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class LauncherActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.launcher_progressBar) ProgressBar progressBar;
    @BindView(R.id.launcher_text) TextView launcherText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initView() {
        launcherText.setText("CHECKING DEVICE COMPATIBILITY...");
        progressBar.setVisibility(View.VISIBLE);
    }


}
