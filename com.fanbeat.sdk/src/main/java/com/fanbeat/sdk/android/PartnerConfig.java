package com.fanbeat.sdk.android;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 6/8/16.
 */
public class PartnerConfig {
    public String id;
    public String name;
    public String channel;
    public String team;
    public String promoBackground;
    public String promoText;
    public List<PromoPrize> promoPrizes;
    public String promoLogo;

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

    public static PartnerConfig getDefault()
    {
        PartnerConfig config = new PartnerConfig();

        config.name = "Golf Channel FanBeat";
        config.channel = "golfchannel";
        config.team = "golf__rydercup";
        config.promoBackground = "promo_background";
        config.promoText = "Compete to win awesome prizes by answering predict-the-action and trivia questions. It’s fun and free to play!";
        config.promoPrizes = new ArrayList<>();

        PromoPrize golfBag = new PromoPrize();
        golfBag.icon = "ping_bag_stand";

        PromoPrize callawayWedge = new PromoPrize();
        callawayWedge.icon = "callaway_wedge";

        PromoPrize titleistBalls = new PromoPrize();
        titleistBalls.icon = "titleist_balls";

        config.promoPrizes.add(golfBag);
        config.promoPrizes.add(callawayWedge);
        config.promoPrizes.add(titleistBalls);

        config.promoLogo = "ryder_cup";

        return config;
    }

    public boolean isGolfChannel() {
        return channel != null && channel.equals("golfchannel");
    }
}
