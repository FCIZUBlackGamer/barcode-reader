package com.google.android.gms.samples.vision.barcodereader.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SimpleView extends View {

    Paint paint = new Paint();
    RectF bodyRect = new RectF();

    public SimpleView(Context context) {
        this(context, null);
    }

    public SimpleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bodyRect = new RectF(10, 10, getWidth()-10, getHeight()-10);

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(bodyRect,paint);
    }
}
