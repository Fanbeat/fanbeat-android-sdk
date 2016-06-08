package com.fanbeat.sdk.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * Created by tony on 6/7/16.
 */
class DeepLinker {
    private static final String BRANCH_LIVE_KEY = "key_live_oam5GSs8U81sJ8TvPo8v6bbpDudyMBQN";
    private static final String BRANCH_TEST_KEY = "key_test_mbo9SGq4LZXBQ9HvTj89lgcgzvnwJDN0";

    private static DeepLinker mInstance = null;

    private Context mContext = null;
    private boolean mIsLive;
    private WeakReference<DeepLinkerListener> mDeferredListener;
    private PartnerConfig mConfig;

    private DeepLinker(Context context) {
        mContext = context;
    }

    public static DeepLinker getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DeepLinker(context);

        return mInstance;
    }

    public PartnerConfig getConfig() {
        return mConfig;
    }

    public void setConfig(PartnerConfig config) {
        mConfig = config;
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
        // get an instance of branch with our key
        Branch branch = Branch.getInstance(mContext,
                mIsLive ? BRANCH_LIVE_KEY : BRANCH_TEST_KEY);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(partnerId)
                .setTitle("FanBeat");
        LinkProperties linkProperties = new LinkProperties()
                .setChannel(partnerId)
                .setFeature("SDK")
                .addControlParameter("partner_id", partnerId);

        if (userId != null)
            linkProperties.addControlParameter("partner_user_id", userId);

        branchUniversalObject.generateShortUrl(mContext, linkProperties, listener);
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
