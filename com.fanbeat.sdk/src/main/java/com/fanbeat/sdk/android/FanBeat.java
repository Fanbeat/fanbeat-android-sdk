package com.fanbeat.sdk.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by tony on 6/7/16.
 */
public class FanBeat {
    private static final String FANBEAT_METADATA_KEY = "com.fanbeat.sdk.FanBeatID";

    private static FanBeat mInstance = null;

    private Context mContext;
    private String mPartnerId;
    private String mUserId;
    private WeakReference<FanBeatListener> mDefferedListener;

    private FanBeat(@NonNull Context context) {
        mContext = context;

        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object id = info.metaData.get(FANBEAT_METADATA_KEY);
            mPartnerId = id.toString();

            if (mPartnerId == null) {
                Log.i("FanBeat SDK", FANBEAT_METADATA_KEY + " not found in the AndroidManifest");
            } else {
                new PartnerConfigTask().execute(mPartnerId);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("FanBeat SDK", "Error loading context package");
        }
    }

    public static FanBeat getInstance() {
        if (mInstance == null) {
            Log.e("FanBeat SDK", "FanBeat.getInstance(Context) must be called before calling this overload");
        }

        return mInstance;
    }

    public static FanBeat getInstance(@NonNull Context context) {
        if (mInstance == null)
            mInstance = new FanBeat(context);

        return mInstance;
    }

    public static FanBeat getInstance(@NonNull Context context, @NonNull String partnerId) {
        FanBeat instance = getInstance(context);
        instance.mPartnerId = partnerId;

        new PartnerConfigTask().execute(partnerId);

        return instance;
    }

    protected void setPartnerConfig(PartnerConfig config) {
        DeepLinker.getInstance(mContext).setConfig(config);
    }

    public void open() {
        open(null);
    }

    public void open(@Nullable FanBeatListener listener) {
        openForUser(null, listener);
    }

    public void openForUser(@Nullable String userId) {
        openForUser(userId, null);
    }

    public void openForUser(@Nullable String userId, @Nullable FanBeatListener listener) {
        if (listener != null)
            mDefferedListener = new WeakReference<FanBeatListener>(listener);

        if (mPartnerId == null) {
            Log.i("FanBeat SDK", FANBEAT_METADATA_KEY + " not found in the AndroidManifest");
            finalizeListener(false);
            return;
        }

        DeepLinker deepLinker = DeepLinker.getInstance(mContext);

        if (deepLinker.canOpenFanbeat()) {
            deepLinker.openForUser(mPartnerId, userId);
        } else {
            mUserId = userId;

            final Intent intent = new Intent(mContext, FanBeatPromoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mContext.startActivity(intent);
                }
            });
        }
    }

    protected void onPromoActivityResult(boolean storeOpened) {
        if (!storeOpened) {
            finalizeListener(false);
            return;
        }

        DeepLinker deepLinker = DeepLinker.getInstance(mContext);

        finalizeListener(true);
        deepLinker.openForUser(mPartnerId, mUserId);
    }

    private void finalizeListener(boolean didLaunch) {
        FanBeatListener listener =
                mDefferedListener != null ? mDefferedListener.get() : null;

        if (listener != null)
            listener.onComplete(didLaunch);
    }

    public interface FanBeatListener {
        void onComplete(boolean didLaunch);
    }
}
