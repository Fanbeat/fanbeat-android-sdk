package com.fanbeat.sdk.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

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
            }

            Branch.getAutoInstance(context);

        } catch (PackageManager.NameNotFoundException e) {
            Log.i("FanBeat SDK", "Error loading context package");
        }
    }

    public static FanBeat getInstance(@NonNull Context context) {
        if (mInstance == null)
            mInstance = new FanBeat(context);

        return mInstance;
    }

    public static FanBeat getInstance(@NonNull Context context, @NonNull String partnerId) {
        FanBeat instance = getInstance(context);
        instance.mPartnerId = partnerId;

        return instance;
    }

    public void open() {

    }

    public void open(@NonNull FanBeatListener listener) {
        mDefferedListener = new WeakReference<FanBeatListener>(listener);
        open();
    }

    public void openForUser(@NonNull String userId) {

    }

    public void openForUser(@NonNull String userId, @NonNull FanBeatListener listener) {
        mDefferedListener = new WeakReference<FanBeatListener>(listener);
        openForUser(userId);
    }

    public interface FanBeatListener {
        void onComplete(boolean didLaunch);
    }
}
