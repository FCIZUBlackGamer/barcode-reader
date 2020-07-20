package com.google.android.gms.samples.vision.barcodereader.ui.customviews;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.samples.vision.barcodereader.R;

public class CustomBatteryView extends View {
    private Float radius = 0f;
    private boolean isCharging = false;
    Context context;
    AttributeSet attrs = null;
    int defStyleAttr = 0;
    // Top
    private PaintDrawable topPaint = new PaintDrawable(Color.parseColor("#E74C3C"));// I only want to corner top-left and top-right so I use PaintDrawable instead of Paint
    private Rect topRect = new Rect();
    private int topPaintWidthPercent = 50;
    private int topPaintHeightPercent = 8;

    // Border
    private Paint borderPaint = new Paint();

    private RectF borderRect = new RectF();
    private int borderStrokeWidthPercent = 8;
    private Float borderStroke = 0f;

    // Percent
    private Paint percentPaint = new Paint();
    private RectF percentRect = new RectF();
    private float percentRectTopMin = 0f;
    private int percent = 0;

    // Charging
    private RectF chargingRect = new RectF();
    private Bitmap chargingBitmap = null;

    public CustomBatteryView(Context context){
        super(context);
        this.context = context;
    }
    public CustomBatteryView(Context context, @Nullable AttributeSet attrs){
        super(context,attrs,0);
        this.context = context;
        this.attrs = attrs;
        borderPaint.setColor(Color.parseColor("#0069C0"));
        borderPaint.setStyle(Paint.Style.STROKE);
        init(attrs);
        chargingBitmap = getBitmap(R.drawable.ic_charging);
    }
    public CustomBatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;
        borderPaint.setColor(Color.parseColor("#0069C0"));
        borderPaint.setStyle(Paint.Style.STROKE);
        init(attrs);
        chargingBitmap = getBitmap(R.drawable.ic_charging);
    }


    private void init(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);
        try {
            percent = ta.getInt(R.styleable.BatteryView_bv_percent, 0);
            isCharging = ta.getBoolean(R.styleable.BatteryView_bv_charging, false);
        } finally {
            ta.recycle();
        }
    }
    private void drawTop(Canvas canvas) {
        topPaint.setBounds(topRect);
        float[] floatArray = {radius, radius, radius, radius, 0f, 0f, 0f, 0f} ;
        topPaint.setCornerRadii(floatArray);
        topPaint.draw(canvas);
    }

    private void drawBody(Canvas canvas) {
        borderPaint.setStrokeWidth( borderStroke);
        canvas.drawRoundRect(borderRect, radius, radius, borderPaint);
    }

    private void drawProgress(Canvas canvas, int percent) {
        percentPaint.setColor(getPercentColor(percent));
        percentRect.top = percentRectTopMin + (percentRect.bottom - percentRectTopMin) * (100 - percent) / 100;
        canvas.drawRect(percentRect, percentPaint);
    }

    // todo change color
    private int getPercentColor(int percent) {
//        if (percent > 50) {
//            return Color.WHITE;
//        }
//        if (percent > 30) {
//            return Color.YELLOW;
//        }
        return Color.parseColor("#D54CAF50");
    }

    private void drawCharging(Canvas canvas) {
        if(chargingBitmap !=null)
            canvas.drawBitmap(chargingBitmap, null, chargingRect, null);

    }

    private Bitmap getBitmap( int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId) ;
        Bitmap bitmap =  Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new  Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

   public void charge() {
        isCharging = true;
        invalidate(); // can improve by invalidate(Rect)
    }

   public void unCharge() {
        isCharging = false;
        invalidate();
    }

    public void setPercent(int percent) {
        if (percent > 100 || percent < 0) {
            return;
        }
        this.percent = percent;
        invalidate();
    }

    public int getPercent() {
        return percent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = View.getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int measureHeight = (int) (measureWidth * 1.8f);
        setMeasuredDimension(measureWidth, measureHeight);

        radius = borderStroke / 2;
        borderStroke = Float.valueOf(((borderStrokeWidthPercent * measureWidth)))/ 100;

        // Top
        int topLeft = measureWidth * ((100 - topPaintWidthPercent) / 2) / 100;
        int topRight = measureWidth - topLeft;
        int topBottom = topPaintHeightPercent * measureHeight / 100;
        topRect = new  Rect(topLeft, 0, topRight, topBottom);

        // Border
        float borderLeft = borderStroke / 2;
        float borderTop = topBottom + borderStroke / 2;
        float borderRight = measureWidth - borderStroke / 2;
        float borderBottom = measureHeight - borderStroke / 2;
        borderRect = new RectF(borderLeft, borderTop, borderRight, borderBottom);

        // Progress
        float progressLeft = borderStroke;
        percentRectTopMin = topBottom + borderStroke;
        float progressRight = measureWidth - borderStroke;
        float progressBottom = measureHeight - borderStroke;
        percentRect = new RectF(progressLeft, percentRectTopMin, progressRight, progressBottom);

        // Charging Image
        float chargingLeft = borderStroke;
        float chargingTop = topBottom + borderStroke;
        float chargingRight = measureWidth - borderStroke;
        float chargingBottom = measureHeight - borderStroke;
        float diff = ((chargingBottom - chargingTop) - (chargingRight - chargingLeft));
        chargingTop += diff / 2;
        chargingBottom -= diff / 2;
        chargingRect = new RectF(chargingLeft, chargingTop, chargingRight, chargingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTop(canvas);
        drawBody(canvas);
        if (!isCharging) {
            drawProgress(canvas, percent);
        } else {
            drawCharging(canvas);
        }
    }


}
