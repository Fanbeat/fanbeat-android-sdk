package com.fanbeat.sdk.android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by tony on 9/14/16.
 */
public class FanBeatAnalytics {

    private static final String VIEW_PROMO_EVENT = "activation";
    private static final String INSTALLED_EVENT = "installed";

    private static FanBeatAnalytics mInstance = null;
    private Context mContext;

    private boolean mIsLive = false;

    private FanBeatAnalytics(Context context) {
        mContext = context;
    }

    public static FanBeatAnalytics getInstance() {
        if (mInstance == null) {
            Log.e("FanBeat SDK", "FanBeatAnalytics.getInstance(Context) must be called before calling this overload");
        }

        return mInstance;
    }

    public static FanBeatAnalytics getInstance(@NonNull Context context) {
        if (mInstance == null)
            mInstance = new FanBeatAnalytics(context);

        return mInstance;
    }

    public boolean getIsLive() { return mIsLive; }
    public void setIsLive(boolean isLive) { this.mIsLive = isLive; }

    public void didViewPromoScreen(String partnerId) {
        String url = String.format("%ssdk/%s/%s",
                mContext.getString(mIsLive ? R.string.base_analytics_url : R.string.base_dev_analytics_url),
                partnerId,
                VIEW_PROMO_EVENT);

        new FanBeatAnalyticsTask().execute(url);
    }

    public void didInstallFanBeat(String partnerId) {
        String url = String.format("%ssdk/%s/%s",
                mContext.getString(mIsLive ? R.string.base_analytics_url : R.string.base_dev_analytics_url),
                partnerId,
                INSTALLED_EVENT);

        new FanBeatAnalyticsTask().execute(url);
    }
}
