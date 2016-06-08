package com.fanbeat.sdk.android;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by tony on 6/7/16.
 */
public class FanBeatPromoActivity extends Activity implements FanBeatPromoView.FanBeatPromoViewListener {

    private boolean mWaitingForStore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FanBeatPromoView v = new FanBeatPromoView(this);
        v.addListener(this);
        setContentView(v);
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
}
