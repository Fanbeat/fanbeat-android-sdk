package com.fanbeat.sdk.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by tony on 8/5/16.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;
    private int data = 0;
    private int mWidth = -1;
    private int mHeight = -1;
    private Context mContext;

    public BitmapWorkerTask(ImageView imageView, Context context) {
        this(imageView, context, -1, -1);
    }

    public BitmapWorkerTask(ImageView imageView, Context context, int width, int height) {
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
        mContext = context;
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        return decodeSampledBitmapFromResource(mContext.getResources(), data, mWidth, mHeight);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        if (mWidth > 0 && mHeight > 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        } else {
            return BitmapFactory.decodeResource(res, resId);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
