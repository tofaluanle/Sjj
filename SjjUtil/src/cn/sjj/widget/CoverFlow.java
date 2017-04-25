package cn.sjj.widget;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class CoverFlow extends Gallery {

    public static final int MODE_NONE = 0;
    public static final int MODE_NORMAL = 1;
    public static final int MODE_DISTANCE = 2;
    private int mode = 0;
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();
    private int mMaxRotationAngle = 50;
    private int mMaxZoom = -380;
    private int mCoverFlowCenter;
    private boolean mAlphaMode = true;
    private boolean mCircleMode = false;
    private int zoomRate = 160;

    int lastPosition;

    public CoverFlow(Context context) {
        super(context);
        init();
    }

    public CoverFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (android.os.Build.VERSION.SDK_INT <= 15) {
            setStaticTransformationsEnabled(true);
        }
    }

    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setZoomRate(int zoomRate) {
        this.zoomRate = zoomRate;
    }

    public boolean getCircleMode() {
        return mCircleMode;
    }

    public void setCircleMode(boolean isCircle) {
        mCircleMode = isCircle;
    }

    public boolean getAlphaMode() {
        return mAlphaMode;
    }

    public void setAlphaMode(boolean isAlpha) {
        mAlphaMode = isAlpha;
    }

    public int getMaxZoom() {
        return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }

    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (android.os.Build.VERSION.SDK_INT > 15) {
            return false;
        } else {
            switch (mode) {
                case MODE_NORMAL:
                    scaleNormal(child, t);
                    break;
                case MODE_DISTANCE:
                    scaleByDistance(child, t);
                    break;
            }
        }
        return true;
    }

    private void scaleByDistance(View child, Transformation t) {
        // final int mCenter = (getWidth() - getPaddingLeft() -
        // getPaddingRight()) / 2
        // + getPaddingLeft();
        final int childCenter = child.getLeft() + child.getWidth() / 2;
        final int childWidth = child.getWidth();

        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);
        float rate = Math.abs((float) (mCoverFlowCenter - childCenter)
                / childWidth);

        mCamera.save();
        final Matrix matrix = t.getMatrix();
        float zoomAmount = (float) (rate * zoomRate);
        mCamera.translate(0.0f, 0.0f, zoomAmount);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-(childWidth / 2), -(childWidth / 2));
        matrix.postTranslate((childWidth / 2), (childWidth / 2));
        mCamera.restore();
    }

    private void scaleNormal(View child, Transformation t) {
        final int childCenter = child.getLeft() + child.getWidth() / 2;
        final int childWidth = child.getWidth();
        final int childHeight = child.getHeight();

        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        float rate = Math.abs((float) (mCoverFlowCenter - childCenter)
                / childWidth);
        if (0 != rate) {
            final Matrix matrix = t.getMatrix();
            matrix.setScale(0.8f, 0.8f);
            matrix.postTranslate((float) childWidth / 10,
                    (float) childHeight / 10);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoverFlowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int mFirstPosition = getFirstVisiblePosition();
        int mSelectedPosition = getSelectedItemPosition();
        int selectedIndex = mSelectedPosition - mFirstPosition;
        if (i == 0) {
            lastPosition = 0;
        }
        int ret = 0;
        if (selectedIndex < 0) {
            return i;
        }
        if (i == childCount - 1) {
            ret = selectedIndex;
        } else if (i >= selectedIndex) {
            lastPosition++;
            ret = childCount - lastPosition;
        } else {
            ret = i;
        }
        return ret;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
        return false;
//		return super.onFling(e1, e2, velocityX, velocityY);
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret;
        if (android.os.Build.VERSION.SDK_INT > 15 && mode != MODE_NONE) {
            scaleByDistance(child);

            final int saveCount = canvas.save();
            canvas.concat(mMatrix);
            ret = super.drawChild(canvas, child, drawingTime);
            canvas.restoreToCount(saveCount);
        } else {
            ret = super.drawChild(canvas, child, drawingTime);
        }
        return ret;
    }

    private void scaleByDistance(View child) {
        final int halfWidth = child.getLeft() + (child.getMeasuredWidth() >> 1);
        final int halfHeight = child.getMeasuredHeight() >> 1;

        final int childCenter = child.getLeft() + child.getWidth() / 2;
        final int childWidth = child.getWidth();
        float rate = Math.abs((float) (mCoverFlowCenter - childCenter) / childWidth);

        mCamera.save();
        float zoomAmount = (float) (rate * zoomRate);
        mCamera.translate(0.0f, 0.0f, zoomAmount);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        mMatrix.preTranslate(-halfWidth, -halfHeight);
        mMatrix.postTranslate(halfWidth, halfHeight);
    }

}
