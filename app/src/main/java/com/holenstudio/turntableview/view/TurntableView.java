package com.holenstudio.turntableview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.holenstudio.turntableview.R;
import com.holenstudio.turntableview.util.ImageUtil;

/**
 * Created by Holen on 2016/6/12.
 */
public class TurntableView extends View {
    private final String TAG = "TurntableView";
    private float mOuterRadius;
    private float mInnerRadius;
    private float mCenterX;
    private float mCenterY;
    private int mWidth;
    private int mHeight;
    private int[] mIconArray;
    private int[] mSelectedIconArray;
    private int currentIcon;
    private OnDragListener mDragListener;
    //箭头的位置，范围为0~360，顶部为0，顺时针方向
    private int mArrowPosition;
    private int mArrowSrc;
    private double rotateDegree = 0;
    private Paint mPaint;
    private float mLastX;
    private float mLastY;
    private VelocityTracker mVelocityTracker;

    public TurntableView(Context context) {
        this(context, null);
    }

    public TurntableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TurntableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TurntableView);
        mOuterRadius = ta.getFloat(R.styleable.TurntableView_outerRadius, 100);
        mInnerRadius = ta.getFloat(R.styleable.TurntableView_innerRadius, 100);
        mArrowPosition = ta.getInt(R.styleable.TurntableView_arrowPosition, 180);
        mArrowSrc = ta.getResourceId(R.styleable.TurntableView_arrowSrc, R.drawable.arrow);
        init();
    }

    private void init() {
        currentIcon = R.drawable.auto_selected;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        mPaint.setStrokeWidth(1.0f);
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
        canvas.drawCircle(mCenterX, mCenterY, mOuterRadius, mPaint);
        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, mPaint);
        Bitmap seletedBmp = BitmapFactory.decodeResource(getResources(), currentIcon);
        canvas.drawBitmap(seletedBmp, mCenterX - seletedBmp.getWidth() / 2, mCenterY - seletedBmp.getHeight() / 2, mPaint);
        //绘制箭头
        Bitmap arrow = BitmapFactory.decodeResource(getResources(), mArrowSrc);
        float arrowLeft = mCenterX - arrow.getWidth() / 2;
        float arrowTop = mCenterY - mOuterRadius - arrow.getHeight() * 3 / 4;
        canvas.rotate(mArrowPosition, mCenterX, mCenterY);
        canvas.drawBitmap(ImageUtil.rotatingImageView(180, arrow), arrowLeft, arrowTop, mPaint);
        drawIcons(canvas);

        super.onDraw(canvas);
    }

    private void drawIcons(Canvas canvas) {
        canvas.save();
        if (mIconArray != null && mIconArray.length > 0) {
            canvas.rotate((float) rotateDegree, mCenterX, mCenterY);
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
                canvas.drawBitmap(icon, (float) iconLeft, (float) iconTop, mPaint);
                canvas.rotate(360.0f / length * (length - 1), mCenterX, mCenterY);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        float currentX;
        float currentY;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();
                //利用余弦公式cosA = (b*b + c*c - a*a )/ 2*b*c简化后得出角度
//                arcCosDegree = ((mCenterX - lastX) * (mCenterX - currentX) + (mCenterY - lastY) * (mCenterY - currentY)) /
//                        Math.sqrt(((mCenterX - lastX) * (mCenterX - lastX) + (mCenterY - lastY) * (mCenterY - lastY)) *
//                                ((mCenterX - currentX) * (mCenterX - currentX) + (mCenterY - currentY) * (mCenterY - currentY)));
                //利用叉乘|AXB|=x1 * y2 - x2 * y1 = |A| * |B| * sinA得到角度的正弦值
                double arcSinDegree = ((mLastX - mCenterX) * (currentY - mCenterY) - (mLastY - mCenterY) * (currentX - mCenterX)) /
                        Math.sqrt(((mLastX - mCenterX) * (mLastX - mCenterX) + (mLastY - mCenterY) * (mLastY - mCenterY)) * ((currentX - mCenterX) *
                                (currentX - mCenterX) + (currentY - mCenterY) * (currentY - mCenterY)));
                rotateDegree += Math.toDegrees(Math.asin(arcSinDegree));
                rotateDegree %= 360;
                Log.d(TAG, "rotateDegree = " + rotateDegree);
                //将选择的icon放大
                zoomOutSelectedIcon();
                invalidate();
                mLastX = event.getX();
                mLastY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < mSelectedIconArray.length; i++) {
                    if (currentIcon == mSelectedIconArray[i]) {
                        rotateDegree = 360 / mSelectedIconArray.length * i;
                    }
                }
                mVelocityTracker.computeCurrentVelocity(1000, 5000.0f);
                Log.d(TAG, "VelocityTracker:X=" + mVelocityTracker.getXVelocity() + ",Y=" + mVelocityTracker.getYVelocity());
                return true;

        }

        return super.onTouchEvent(event);
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void zoomOutSelectedIcon() {
        int minRange;
        int maxRange;
        for (int i = 0; i < mIconArray.length; i++) {
            minRange = i * 360 / mIconArray.length - 360 / mIconArray.length / 2;
            maxRange = i * 360 / mIconArray.length + 360 / mIconArray.length / 2;
            //这里因为第一个图标的范围是- 360 / mIconArray.length / 2到360 / mIconArray.length / 2，
            //所以当旋转角度大于360 - 360 / mIconArray.length / 2时会判断失误，所以作此处理
            if (minRange < 0) {
                maxRange = 360 + 360 / mIconArray.length / 2;
            }
            if ((minRange <= (rotateDegree + 360) % 360) && (maxRange >= (rotateDegree + 360) % 360)) {
                currentIcon = mSelectedIconArray[i];
                if (mDragListener != null) {
                    mDragListener.onDragFinished(i);
                }
            }
        }
    }

    public void setIconArray(int[] array) {
        mIconArray = array;
        invalidate();
    }

    public void setSelectedIconArray(int[] array) {
        mSelectedIconArray = array;
        invalidate();
    }

    public void setOnDragListener(OnDragListener listener) {
        mDragListener = listener;
    }

    public interface OnDragListener {
        public void onDragFinished(int position);
    }
}
