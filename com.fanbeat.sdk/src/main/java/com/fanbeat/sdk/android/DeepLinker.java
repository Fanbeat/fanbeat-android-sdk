package com.fanbeat.sdk.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * Created by tony on 6/7/16.
 */
class DeepLinker {
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
        if (mContext == null) {
            return PartnerConfig.getDefault();
        }

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
        try {
            String packageId = mContext.getString(R.string.fanbeat_app_package);
            int[] gids = mContext.getPackageManager().getPackageGids(packageId);
            return gids != null && gids.length > 0;
        } catch(PackageManager.NameNotFoundException e) {
            Log.i("FanBeat SDK", "FanBeat app not installed");
        }

        return false;
    }

    private void getBranchUrl(@NonNull String partnerId, @Nullable String userId, Branch.BranchLinkCreateListener listener) {
        // get an instance of branch with our key
        Branch branch = Branch.getInstance(mContext,
                mContext.getString(mIsLive ? R.string.branch_live_key : R.string.branch_test_key));

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(partnerId)
                .setTitle("FanBeat");
        LinkProperties linkProperties = new LinkProperties()
                .setChannel(partnerId)
                .setFeature("SDK")
                .addControlParameter("partner_id", partnerId);

        if (userId != null)
            linkProperties.addControlParameter("partner_user_id", userId);

        if (mConfig != null) {
            String deepLinkPath = mConfig.getDeepLinkPath();
            if (!TextUtils.isEmpty(deepLinkPath)) {
                linkProperties.addControlParameter("$deeplink_path", deepLinkPath);
            }
        }

        branchUniversalObject.generateShortUrl(mContext, linkProperties, listener);
    }

    private void openUrl(String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mContext.startActivity(intent);

                finalizeListener(true);
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
