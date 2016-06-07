package com.fanbeat.sdk.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.branch.indexing.BranchUniversalObject;

/**
 * Created by tony on 6/7/16.
 */
public class DeepLinker {
    private static DeepLinker mInstance = null;

    private Context mContext = null;

    private DeepLinker(Context context) {
        mContext = context;
    }

    public static DeepLinker getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DeepLinker(context);

        return mInstance;
    }

    public void open(@NonNull String partnerId) {

    }

    public void openForUser(@NonNull String partnerId, @NonNull String userId) {

    }

    private boolean canOpenFanbeat() {
        return false;
    }

    private String getBranchUrl(@NonNull String partnerId, @Nullable String userId) {
        return null;
    }
}
