package com.moto.pixelr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;
import com.moto.pixelr.ui.PixelAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class SelectorActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.camera_flash_seek_bar) SeekBar flashSeekBar;
    @BindView(R.id.pixel_selector_recycler_view) RecyclerView pixelRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        unbinder = ButterKnife.bind(this);

        initView();
        initCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initView() {
        initRecyclerView();
        initSeekbar();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pixelRecyclerView.setHasFixedSize(true);
        pixelRecyclerView.setLayoutManager(layoutManager);
        PixelAdapter pixelAdapter = new PixelAdapter(this);
        pixelRecyclerView.setAdapter(pixelAdapter);
    }

    private void initSeekbar() {
        flashSeekBar.setProgress(50); // Sets the progress bar at 50%.
    }

    private void initCamera() {
        // TODO: Initialize camera here.
    }
}
