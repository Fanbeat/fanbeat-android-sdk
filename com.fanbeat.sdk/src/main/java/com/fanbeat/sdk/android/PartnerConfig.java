package com.fanbeat.sdk.android;

import android.text.TextUtils;

/**
 * Created by tony on 6/8/16.
 */
public class PartnerConfig {
    public String id;
    public String name;
    public String channel;
    public String team;

    public String getDeepLinkPath() {
        if (TextUtils.isEmpty(channel)) {
            return null;
        }

        if (!TextUtils.isEmpty(team)) {
            return String.format("team/%s/%s", channel, team);
        } else {
            return String.format("channel/%s", channel);
        }
    }
}
