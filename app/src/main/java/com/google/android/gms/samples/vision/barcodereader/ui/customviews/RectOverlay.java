package com.google.android.gms.samples.vision.barcodereader.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.bobekos.bobek.scanner.overlay.BarcodeOverlay;

import org.jetbrains.annotations.NotNull;

public class RectOverlay extends View implements BarcodeOverlay {
    Rect rect;
    private Paint mRectPaint;

    public RectOverlay(Context context) {
        super(context);
        setWillNotDraw(false);

        mRectPaint = new Paint();
        mRectPaint.setColor(Color.BLACK);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(4.0f);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (rect !=null) {
            canvas.drawRect(rect, mRectPaint);
        }
    }

    @Override
    public void onUpdate(@NotNull Rect posRect, @NotNull String s) {
        rect = posRect;
        invalidate();
    }
}
