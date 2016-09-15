package com.fanbeat.sdk.android;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
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

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        PartnerConfig config = FanBeat.getInstance().getPartnerConfig();

        FanBeatPromoFragment fragment = config != null && config.isGolfChannel() ?
                new GolfChannelPromoFragment() : new FanBeatPromoFragment();
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
    public void onDestroy() {
        super.onDestroy();

        if (!mWaitingForStore) {
            FanBeat.getInstance().onPromoActivityResult(false);
        }
    }

    @Override
    public void onPlayNow() {
        mWaitingForStore = true;
        FanBeat.getInstance().onPlayNowClicked();
    }
}
