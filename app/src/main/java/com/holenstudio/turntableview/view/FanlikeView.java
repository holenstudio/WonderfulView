package com.holenstudio.turntableview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.holenstudio.turntableview.R;
import com.holenstudio.turntableview.model.OvalItem;

import java.util.List;

/**
 * Created by Holen on 2016/6/14.
 */
public class FanlikeView extends View implements View.OnLongClickListener {
    private final static String TAG = "FanlikeView";

    private Context mContext;
    private float mOuterRadius;
    private float mInnerRadius;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private boolean isVisible = false;
    private float mCenterX;
    private float mCenterY;
    private long mTouchDownTime;
    private long mCurrentTime;
    private List<OvalItem> mOvalItems;
    private RectF mOutterOval;
    private RectF mInnerOval;

    public FanlikeView(Context context) {
        this(context, null);
    }

    public FanlikeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FanlikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.WonderfulView);
        mOuterRadius = ta.getFloat(R.styleable.WonderfulView_outerRadius, 100);
        mInnerRadius = ta.getFloat(R.styleable.WonderfulView_innerRadius, 100);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        mPaint.setStrokeWidth(1.0f);
        setOnLongClickListener(this);

        mOutterOval = new RectF(mCenterX - mOuterRadius, mCenterY - mOuterRadius, mCenterX + mOuterRadius, mCenterY + mOuterRadius);
        mInnerOval = new RectF(mCenterX - mInnerRadius, mCenterY - mInnerRadius, mCenterX + mInnerRadius, mCenterY + mInnerRadius);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measure(widthMeasureSpec);
        mHeight = measure(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private int measure(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int resultMeasure;
        if (mode == MeasureSpec.EXACTLY) {
            resultMeasure = size;
        } else {
            resultMeasure = 200;
        }
        return resultMeasure;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isVisible) {
            return;
        }
        if (mOvalItems == null || mOvalItems.isEmpty()) {
            return;
        }
        int size = mOvalItems.size();
        if (size > 6 || size < 0) {
            return;
        }
        float angle = 150 / size;
        for (int i = 0; i < size; i++) {
            canvas.drawArc(mOutterOval, angle * (i + 1), angle * (i + 2), true, mPaint);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX;
        float currentY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownTime = System.currentTimeMillis();
                mCenterX = event.getX();
                mCenterY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurrentTime = System.currentTimeMillis();
                currentX = event.getX();
                currentY = event.getY();
                if (mCurrentTime - mTouchDownTime < 3) {
                    isVisible = false;
                } else if ((Math.abs(mCenterX - currentX) < 20) && (Math.abs(mCenterY - currentY) < 20)){
                    isVisible = true;
                }

                invalidate();

                break;
            case MotionEvent.ACTION_UP:

                return true;

        }

        return super.onTouchEvent(event);
    }

    public void hide() {
        isVisible = false;
        setVisibility(View.INVISIBLE);
    }

    public void show() {
        isVisible = true;
        setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
