package com.fanbeat.sdk.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.List;

/**
 * Created by tony on 6/7/16.
 */
public class FanBeatPromoActivity extends Activity implements FanBeatPromoView.FanBeatPromoViewListener {
    private static final String FANBEAT_PACKAGE_ID = "com.bluefletch.ingame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FanBeatPromoView v = new FanBeatPromoView(this);
        v.addListener(this);
        setContentView(v);
    }

    @Override
    public void onPlayNow() {
        if (launchPlayStore(FANBEAT_PACKAGE_ID)) {
            return;
        }

        launchPlayStoreOnline(FANBEAT_PACKAGE_ID);
    }

    private boolean launchPlayStore(String packageId) {
        if (packageId == null || packageId.isEmpty())
            return false;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId));

        // 3rd party apps may use the "market://" URI scheme, way to go Android!
        // pull in a list of all apps that support it and try to find the play store app
        final List<ResolveInfo> marketApps = getPackageManager().queryIntentActivities(intent, 0);
        for(ResolveInfo app: marketApps) {
            ActivityInfo activityInfo = app.activityInfo;
            if (activityInfo.applicationInfo.packageName.equals("com.android.vending")) {
                ComponentName componentName = new ComponentName(
                        activityInfo.applicationInfo.packageName,
                        activityInfo.name
                );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setComponent(componentName);
                startActivity(intent);
                return true;
            }
        }

        return false;
    }

    private boolean launchPlayStoreOnline(String packageId) {
        if (packageId == null || packageId.isEmpty())
            return false;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageId));
        startActivity(intent);
        return true;
    }
}
