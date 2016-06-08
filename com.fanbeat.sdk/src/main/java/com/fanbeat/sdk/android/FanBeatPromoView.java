package com.fanbeat.sdk.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 6/7/16.
 */
class FanBeatPromoView extends RelativeLayout {
    private List<WeakReference<FanBeatPromoViewListener>> mListeners = new ArrayList<>();

    public FanBeatPromoView(Context context) {
        super(context);
        initialize(context);
    }

    public FanBeatPromoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public void addListener(FanBeatPromoViewListener listener) {
        mListeners.add(new WeakReference<FanBeatPromoViewListener>(listener));
    }

    private void initialize(Context context) {
        inflate(context, R.layout.fanbeat_promo_view, this);

        Button playNowButton = (Button) findViewById(R.id.playNowButton);
        playNowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for(WeakReference<FanBeatPromoViewListener> ref : mListeners) {
                    FanBeatPromoViewListener listener = ref.get();
                    if (listener != null) {
                        listener.onPlayNow();
                    }
                }
            }
        });
    }

    protected interface FanBeatPromoViewListener {
        void onPlayNow();
    }
}
