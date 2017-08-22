package com.kokaihop.utility.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

/**
 * Created by Vaibhav Chahal on 17/8/17.
 */

public class RoundedCornersTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    private int mRadius;
    private int mDiameter;
    private int mMargin;

    public RoundedCornersTransformation(Context context, int radius, int margin) {
        mBitmapPool = Glide.get(context).getBitmapPool();
        mRadius = radius;
        mDiameter = mRadius * 2;
        mMargin = margin;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - mMargin;
        float bottom = height - mMargin;
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
    }


    public String getId() {
        return "RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter="
                + mDiameter + ")";
    }
}
