package com.fanbeat.sdk.android;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by tony on 9/15/16.
 */
public class GolfChannelPromoFragment extends FanBeatPromoFragment {

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        Typeface font = getFontFromRes(R.raw.gcfrankbold);

        View view = layoutInflater.inflate(R.layout.golf_channel_promo_view, container, false);

        Button button = (Button)view.findViewById(R.id.golfGetGame);
        button.setTypeface(font);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(WeakReference<FanBeatPromoViewListener> ref : mListeners) {
                    FanBeatPromoViewListener listener = ref.get();
                    if (listener != null) {
                        listener.onPlayNow();
                    }
                }
            }
        });

        TextView competeLabel = (TextView)view.findViewById(R.id.golfCompeteLabel);
        TextView itsFunLabel = (TextView)view.findViewById(R.id.golfItsFunLabel);

        competeLabel.setTypeface(font);
        itsFunLabel.setTypeface(font);

        return view;
    }

    private Typeface getFontFromRes(int resource)
    {
        Typeface tf = null;
        InputStream is = null;
        try {
            is = getResources().openRawResource(resource);
        }
        catch(Resources.NotFoundException e) {
            Log.e("FanBeat", "Could not find font in resources!");
        }

        String outPath = getActivity().getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

        try
        {
            byte[] buffer = new byte[is.available()];
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

            int l = 0;
            while((l = is.read(buffer)) > 0)
                bos.write(buffer, 0, l);

            bos.close();

            tf = Typeface.createFromFile(outPath);

            // clean up
            new File(outPath).delete();
        }
        catch (IOException e)
        {
            Log.e("FanBeat", "Error reading in font!");
            return null;
        }

        Log.d("FanBeat", "Successfully loaded font.");

        return tf;
    }
}
