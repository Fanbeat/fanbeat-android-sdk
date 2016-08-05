package com.fanbeat.sdk.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 8/5/16.
 */
public class FanBeatPromoFragment extends Fragment {

    private List<WeakReference<FanBeatPromoViewListener>> mListeners = new ArrayList<>();

    private ImageView backgroundImageView;
    private TextView promoTextView;
    private ViewPager pager;
    private Button playNowButton;
    private PrizesPagerAdapter pagerAdapter;
    private CirclePageIndicator pageIndicator;

    protected interface FanBeatPromoViewListener {
        void onPlayNow();
    }

    public void addListener(FanBeatPromoViewListener listener) {
        mListeners.add(new WeakReference<FanBeatPromoViewListener>(listener));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.fanbeat_promo_view, container, false);

        backgroundImageView = (ImageView)view.findViewById(R.id.promoBackground);
        promoTextView = (TextView)view.findViewById(R.id.promoText);
        pager = (ViewPager)view.findViewById(R.id.promoPrizesPager);
        pageIndicator = (CirclePageIndicator)view.findViewById(R.id.pageIndicator);
        playNowButton = (Button)view.findViewById(R.id.playNowButton);

        playNowButton.setOnClickListener(new View.OnClickListener() {
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

        updateLayout();

        return view;
    }

    private void updateLayout() {
        String background = "";
        String promoText = "";
        ArrayList<PromoPrize> knownPrizes = new ArrayList<>();

        PartnerConfig partnerConfig = FanBeat.getInstance().getPartnerConfig();

        if (partnerConfig != null) {
            background = partnerConfig.promoBackground;
            promoText = partnerConfig.promoText;

            for (PromoPrize p : partnerConfig.promoPrizes) {
                int id = getResources().getIdentifier(p.icon, "drawable", getActivity().getPackageName());
                if (id > 0) {
                    p.iconResourceId = id;
                    knownPrizes.add(p);
                }
            }

            int backgroundResourceId = getResources().getIdentifier(background, "drawable", getActivity().getPackageName());
            if (backgroundResourceId <= 0) {
                backgroundResourceId = R.drawable.promo_background;
            }

            if (backgroundImageView != null && FanBeatPromoActivity.class.isInstance(getActivity())) {
                ((FanBeatPromoActivity)getActivity()).loadBitmap(backgroundResourceId, backgroundImageView);
            }

            if (promoTextView != null)
                promoTextView.setText(promoText);

            if (pager != null) {
                pager.setOffscreenPageLimit(partnerConfig.promoPrizes.size());
                pager.setAdapter(new PrizesPagerAdapter(getActivity().getSupportFragmentManager(), knownPrizes));
            }

            if (pageIndicator != null) {
                pageIndicator.setRadius(15);
                pageIndicator.setPageColor(Color.argb(100, 255, 255, 255));
                pageIndicator.setFillColor(Color.WHITE);
                pageIndicator.setStrokeColor(Color.TRANSPARENT);
                pageIndicator.setViewPager(pager);
            }
        }
    }

    private class PrizesPagerAdapter extends FragmentPagerAdapter
    {
        private List<PromoPrize> prizes;

        public PrizesPagerAdapter(FragmentManager fm, List<PromoPrize> prizes) {
            super(fm);
            this.prizes = prizes;
        }

        @Override
        public Fragment getItem(int position) {
            return PromoPrizeFragment.newInstance(prizes.get(position).iconResourceId);
        }

        @Override
        public int getCount() {
            return prizes.size();
        }
    }
}
