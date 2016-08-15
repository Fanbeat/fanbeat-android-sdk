package com.fanbeat.sdk.android;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by tony on 8/5/16.
 */
public class PromoPrizeFragment extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "resId";

    private int resourceId;
    private ImageView imageView;

    static PromoPrizeFragment newInstance(int iconResourceId) {
        final PromoPrizeFragment f = new PromoPrizeFragment();
        final Bundle args = new Bundle();
        args.putInt(IMAGE_DATA_EXTRA, iconResourceId);
        f.setArguments(args);
        return f;
    }

    public PromoPrizeFragment() {}

    @Override
    @SuppressLint("ResourceType")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        resourceId = getArguments() == null ? -1 : getArguments().getInt(IMAGE_DATA_EXTRA, 0);

        View view = layoutInflater.inflate(R.layout.fanbeat_promo_prize_fragment, container, false);
        imageView = (ImageView)view.findViewById(R.id.promoPrizeIcon);

        imageView.setImageResource(resourceId);

        return view;
    }
}
