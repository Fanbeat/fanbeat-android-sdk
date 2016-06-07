package com.fanbeat.sdk.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Created by tony on 6/7/16.
 */
public class DeepLinker {
    private static final String BRANCH_LIVE_KEY = "key_live_oam5GSs8U81sJ8TvPo8v6bbpDudyMBQN";
    private static final String BRANCH_TEST_KEY = "key_test_mbo9SGq4LZXBQ9HvTj89lgcgzvnwJDN0";

    private static DeepLinker mInstance = null;

    private Context mContext = null;
    private boolean mIsLive;
    private WeakReference<DeepLinkerListener> mDeferredListener;

    private DeepLinker(Context context) {
        mContext = context;
    }

    public static DeepLinker getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DeepLinker(context);

        return mInstance;
    }

    public boolean getIsLive() {
        return mIsLive;
    }

    public void setIsLive(boolean isLive) {
        mIsLive = isLive;
    }

    public void open(@NonNull String partnerId) {
        openForUser(partnerId, null);
    }

    public void openForUser(@NonNull String partnerId, @NonNull String userId) {
        getBranchUrl(partnerId, userId, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error != null) {
                    finalizeListener(false);
                    return;
                }

                openUrl(url);
            }
        });
    }

    public boolean canOpenFanbeat() {
        return false;
    }

    private void getBranchUrl(@NonNull String partnerId, @Nullable String userId, Branch.BranchLinkCreateListener listener) {
        Branch branch = Branch.getInstance(mContext,
                mIsLive ? BRANCH_LIVE_KEY : BRANCH_TEST_KEY);

        JSONObject params = new JSONObject();
        try {
            params.put("partner_id", partnerId);

            if (userId != null) {
                params.put("partner_user_id", userId);
            }
        } catch (JSONException ex) {
            Log.e("FanBeat SDK", "Error creating Branch URL parameters");
        }

        branch.getShortUrl(partnerId, "SDK", null, params, listener);
    }

    private void openUrl(String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        finalizeListener(true);

        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mContext.startActivity(intent);
            }
        });
    }

    private void finalizeListener(boolean success) {
        DeepLinkerListener listener =
                mDeferredListener != null ? mDeferredListener.get() : null;

        if (listener != null)
            listener.onComplete(success);
    }

    protected interface DeepLinkerListener {
        void onComplete(boolean success);
    }
}
