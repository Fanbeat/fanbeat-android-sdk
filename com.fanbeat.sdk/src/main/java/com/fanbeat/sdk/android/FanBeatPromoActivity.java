package com.fanbeat.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by tony on 6/7/16.
 */
public class FanBeatPromoActivity extends FragmentActivity implements FanBeatPromoFragment.FanBeatPromoViewListener {

    private boolean mWaitingForStore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PartnerConfig config = FanBeat.getInstance().getPartnerConfig();

        FanBeatPromoFragment fragment = new FanBeatPromoFragment();
        fragment.addListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mWaitingForStore) {
            FanBeat.getInstance().onPromoActivityResult(true);
            finish();
        }
    }

    @Override
    public void onPlayNow() {
        FanBeat.getInstance().onPromoActivityResult(true);
        finish();
    }

    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, this);
        task.execute(resId);
    }

    public void loadBitmap(int resId, ImageView imageView, int width, int height) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, this, width, height);
        task.execute(resId);
    }
}
