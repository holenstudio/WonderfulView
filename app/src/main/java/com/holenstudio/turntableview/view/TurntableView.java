package com.holenstudio.turntableview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.View;

import com.holenstudio.turntableview.R;
import com.holenstudio.turntableview.util.ImageUtil;

/**
 * Created by Holen on 2016/6/12.
 */
public class TurntableView extends View{
    private final String TAG = "TurntableView";
    private float mOuterRadius;
    private float mInnerRadius;
    private float mCenterX;
    private float mCenterY;
    private int mWidth;
    private int mHeight;
    private int[] mIconArray;
    private OnDragListener mDragListener;
    //箭头的位置，范围为0~360，顶部为0，顺时针方向
    private int mArrowPosition;
    private int mArrowSrc;

    public TurntableView(Context context) {
        this (context, null);
    }

    public TurntableView(Context context, AttributeSet attrs) {
        this (context, attrs , 0);
    }

    public TurntableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TurntableView);
        mOuterRadius = ta.getFloat(R.styleable.TurntableView_outerRadius, 100);
        mInnerRadius = ta.getFloat(R.styleable.TurntableView_innerRadius, 100);
        mArrowPosition = ta.getInt(R.styleable.TurntableView_arrowPosition, 180);
        mArrowSrc = ta.getResourceId(R.styleable.TurntableView_arrowSrc, R.drawable.arrow);
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
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(1.0f);
        canvas.drawCircle(mCenterX, mCenterY, mOuterRadius, paint);
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, paint);
        Bitmap arrow = BitmapFactory.decodeResource(getResources(), mArrowSrc);
        float arrowLeft = mCenterX + mOuterRadius * (float) Math.sin(mArrowPosition * 1.0 / 180 * Math.PI) - arrow.getWidth() / 2;
        float arrowTop = mCenterY - mOuterRadius * (float) Math.cos(mArrowPosition * 1.0 / 180 * Math.PI);
        canvas.drawBitmap(ImageUtil.rotatingImageView((mArrowPosition + 180) % 360, arrow), arrowLeft, arrowTop, paint);
        if (mIconArray != null && mIconArray.length > 0) {
            int length = mIconArray.length;
            double iconLeft;
            double iconTop;
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), mIconArray[0]);
            //每一个图标的top到圆中心的距离
            double iconLength = mInnerRadius + (mOuterRadius - mInnerRadius) / 2 + bmp.getHeight() / 2;
            //每一个图标的左上角到圆中心的距离
            double iconRadius = Math.sqrt(bmp.getWidth() * bmp.getWidth() / 4 + iconLength * iconLength);
            double offsetDegree = Math.acos(iconLength / iconRadius);
            for (int i = 0; i < length; i++) {
                Bitmap icon = BitmapFactory.decodeResource(getResources(), mIconArray[i]);
                iconLeft = mCenterX + iconRadius * Math.sin(0 - offsetDegree);
                iconTop = mCenterY - iconRadius * Math.cos(0 - offsetDegree);
                canvas.rotate(i * 360.0f / length, mCenterX, mCenterY);
                canvas.drawBitmap(icon, (float)iconLeft, (float)iconTop, paint);
            }
        }

        super.onDraw(canvas);
    }

    public void setIconArray(int[] array) {
        mIconArray = array;
        invalidate();
    }

    public void setOnDragListener (OnDragListener listener) {
        mDragListener = listener;
    }

    public interface OnDragListener{
        public void onDragFinished(int position);
    }
}
