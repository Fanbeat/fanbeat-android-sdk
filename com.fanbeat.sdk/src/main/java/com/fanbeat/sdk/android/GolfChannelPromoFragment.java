package com.fanbeat.sdk.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;

/**
 * Created by tony on 9/15/16.
 */
public class GolfChannelPromoFragment extends FanBeatPromoFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.golf_channel_promo_view, container, false);

        Button button = (Button)view.findViewById(R.id.golfGetGame);
        button.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }
}
