package slideDampongAnimationLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.dabutaizha.slidedampinganimationactivity.R;

/**
 * Copyright (C) 2018 Unicorn, Inc.
 * Description :
 * Created by dabutaizha on 2018/7/21 上午9:15.
 */

public class SlideDampingAnimationLayout extends FrameLayout {

    Context mContext;
    int screenHeight;
    int screenWidth;

    PointF actionDownPoint;

    /**
     * Description: 绘制贝塞尔曲线控制点
     */
    BezierCurve mBezierCurve;

    /**
     * Description: 边缘的定义范围
     */
    double borderMargin;

    boolean isBorderSliding = false;
    boolean leftSlide = false;
    boolean rightSlide = false;

    int mColor;
    int mBezierType;
    int mAllowGuest;
    boolean mAllowLeft;
    boolean mAllowRight;
    Paint mPathPaint;
    Path mPath;

    Paint mLinePaint;
    float mTransverseLineL, mTransverseLineT, mTransverseLineR, mTransverseLineB;
    float mTopLineL, mTopLineT, mTopLineR, mTopLineB;
    float mBottomLineL, mBottomLineT, mBottomLineR, mBottomLineB;

    SlideEventListener mSlideEventListener;

    public SlideDampingAnimationLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideDampingAnimationLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SlideDampingAnimationLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SlideDampingAnimationLayout,
                defStyleAttr,
                0);
        mColor = ta.getColor(R.styleable.SlideDampingAnimationLayout_bezier_curves_color, Color.BLACK);
        mBezierType = ta.getInt(R.styleable.SlideDampingAnimationLayout_bezier_curves_type, 1);
        mAllowGuest = ta.getInt(R.styleable.SlideDampingAnimationLayout_allow_gesture, 2);
        init(context);
        ta.recycle();
    }

    private void init(Context context) {
        screenHeight = DisplayUtil.getInstance(context).getScreenHeight();
        screenWidth = DisplayUtil.getInstance(context).getScreenWidth();
        borderMargin = screenWidth * 0.02;

        //圆弧画笔
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(mColor);
        mPathPaint.setStyle(Paint.Style.FILL);
        mPathPaint.setStrokeWidth(1f);

        //线段画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.argb(0, 255, 255, 255));
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(5f);

        mPath = new Path();

        if (checkIsQuadratic()) {
            mBezierCurve = new QuadraticBezierCurve();
        } else {

            mBezierCurve = new HighOrderBezierCurve();
        }

        if ((mAllowGuest == 0) || (mAllowGuest == 2)) {
            mAllowLeft = true;
        }
        if ((mAllowGuest == 1) || (mAllowGuest == 2)) {
            mAllowRight = true;
        }

        setWillNotDraw(false);
    }

    /**
     *Description: 是否拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return (checkHandle(event) || super.onTouchEvent(event));
    }

    /**
     *Description: 如何处理事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleEvent(event);
        mPath.reset();
        invalidate();

        return (checkHandle(event) || super.onTouchEvent(event));
    }

    /**
     *Description: 重绘子视图方法
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (checkIsQuadratic()) {
            QuadraticBezierCurve bezierCurve = (QuadraticBezierCurve)mBezierCurve;
            mPath.moveTo(bezierCurve.getStartPoint().x,
                    bezierCurve.getStartPoint().y);
            mPath.quadTo(bezierCurve.getControlPoint().x,
                    bezierCurve.getControlPoint().y,
                    bezierCurve.getEndPoint().x,
                    bezierCurve.getEndPoint().y);
        } else {
            HighOrderBezierCurve bezierCurve = (HighOrderBezierCurve)mBezierCurve;
            mPath.moveTo(bezierCurve.getFirstStartPoint().x,
                    bezierCurve.getFirstStartPoint().y);
            mPath.quadTo(bezierCurve.getFirstControlPoint().x,
                    bezierCurve.getFirstControlPoint().y,
                    bezierCurve.getFirstEndPoint().x,
                    bezierCurve.getFirstEndPoint().y);
            mPath.quadTo(bezierCurve.getSecondControlPoint().x,
                    bezierCurve.getSecondControlPoint().y,
                    bezierCurve.getSecondEndPoint().x,
                    bezierCurve.getSecondEndPoint().y);
            mPath.quadTo(bezierCurve.getThirdControlPoint().x,
                    bezierCurve.getThirdControlPoint().y,
                    bezierCurve.getThirdEndPoint().x,
                    bezierCurve.getThirdEndPoint().y);
        }

        canvas.drawPath(mPath, mPathPaint);
        canvas.drawLine(mTransverseLineL,
                mTransverseLineT,
                mTransverseLineR,
                mTransverseLineB,
                mLinePaint);

        canvas.drawLine(mBottomLineL,
                mBottomLineT,
                mBottomLineR,
                mBottomLineB,
                mLinePaint);

        canvas.drawLine(mTopLineL,
                mTopLineT,
                mTopLineR,
                mTopLineB,
                mLinePaint);
    }

    /**
     * Description: 检测是否处理事件
     */
    boolean checkHandle(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                actionDownPoint = new PointF(event.getX(), event.getY());
                leftSlide = actionDownPoint.x < borderMargin;
                rightSlide = screenWidth - actionDownPoint.x < borderMargin;
                if (leftSlide || rightSlide) {
                    isBorderSliding = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (isBorderSliding) {
                    isBorderSliding = false;
                    return true;
                }
                break;
            default:
                break;
        }
        return isBorderSliding;
    }

    void handleEvent(MotionEvent event) {
        if (checkIsQuadratic()) {
            quadraticBezierHandleEvent(event);
        } else {
            highOrderBezierHandleEvent(event);
        }
    }

    private void quadraticBezierHandleEvent(MotionEvent event) {
        QuadraticBezierCurve bezierCurve = (QuadraticBezierCurve) mBezierCurve;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                actionDownPoint = new PointF(event.getX(), event.getY());

                if (leftSlide && mAllowLeft) {
                    bezierCurve.setStartPoint(new PointF(0, actionDownPoint.y + 150));
                    bezierCurve.setEndPoint(new PointF(0, actionDownPoint.y - 150));
                    float stretchDegree = (event.getX() * 5) / screenWidth;
                    if (event.getX() >= screenWidth / 5) {
                        //贝塞尔曲线控制点不再增长
                        mPathPaint.setAlpha((int) (0.8 * 255));
                        bezierCurve.setControlPoint(new PointF(150, actionDownPoint.y));
                        //直线转为折现 计算折现角度
                        float moveRange = actionDownPoint.x - screenWidth / 5;
                        float moveDegree = moveRange / (screenWidth / 5);
                        int degree;
                        if (moveDegree >= 1) {
                            degree = 45;
                        } else {
                            degree = (int) (moveDegree * 45);
                        }
                        //修改画笔颜色 去除直线痕迹
                        mLinePaint.setColor(Color.argb(255, 255, 255, 255));
                        mTransverseLineL = mTransverseLineR = mTransverseLineT = mTransverseLineB = 0;
                        //计算上半折线坐标
                        mBottomLineL = mTopLineL = 30;
                        mBottomLineT = mTopLineT = actionDownPoint.y;
                        float topLineDegree = (float) (((-degree) * Math.PI) / 180);
                        float topDegreeCos = (float) Math.cos(topLineDegree);
                        float topDegreeSin = (float) Math.sin(topLineDegree);
                        mTopLineR = -20 * (topDegreeSin) + 30;
                        mTopLineB = 20 * (topDegreeCos) + event.getY();
                        //计算下半折线坐标
                        float bottomLineDegree = (float) (((degree + 180) * Math.PI) / 180);
                        float bottomDegreeCos = (float) Math.cos(bottomLineDegree);
                        float bottomDegreeSin = (float) Math.sin(bottomLineDegree);
                        mBottomLineR = -20 * bottomDegreeSin + 30;
                        mBottomLineB = 20 * bottomDegreeCos + event.getY();
                    } else {
                        //计算曲线的变化
                        mPathPaint.setAlpha((int)(stretchDegree * 0.8 * 255));
                        bezierCurve.setControlPoint(new PointF(stretchDegree * 150, actionDownPoint.y));
                        //计算横线的变化
                        mLinePaint.setColor(Color.argb((int)(stretchDegree * 255), 255, 255, 255));
                        float slideRangeX = stretchDegree * 30;
                        float slideRangeY = stretchDegree * 20;
                        mTransverseLineL = mTransverseLineR = slideRangeX;
                        mTransverseLineT = actionDownPoint.y + slideRangeY;
                        mTransverseLineB = actionDownPoint.y - slideRangeY;
                        //归零折现的变化
                        mTopLineL = mTopLineT = mTopLineR = mTopLineB = 0;
                        mBottomLineL = mBottomLineT = mBottomLineR = mBottomLineB = 0;
                    }
                }

                if (rightSlide && mAllowRight) {
                    bezierCurve.setStartPoint(new PointF(screenWidth, actionDownPoint.y + 150));
                    bezierCurve.setEndPoint(new PointF(screenWidth, actionDownPoint.y - 150));
                    float stretchDegree = ((screenWidth - event.getX()) * 5) / screenWidth;
                    if (screenWidth - event.getX() >= screenWidth / 5) {
                        //贝塞尔曲线控制点不再增长
                        mPathPaint.setAlpha((int)(0.8 * 255));
                        bezierCurve.setControlPoint(new PointF(screenWidth - 150, actionDownPoint.y));
                        //直线转为折现 计算折现角度
                        float moveRange = (screenWidth - actionDownPoint.x) - screenWidth / 5;
                        float moveDegree = moveRange / (screenWidth / 5);
                        int degree;
                        if (moveDegree >= 1) {
                            degree = 45;
                        } else {
                            degree = (int) (moveDegree * 45);
                        }
                        //修改画笔颜色 去除直线痕迹
                        mLinePaint.setColor(Color.argb(255, 255, 255, 255));
                        mTransverseLineL = mTransverseLineR = mTransverseLineT = mTransverseLineB = 0;
                        //计算上半折线坐标
                        mBottomLineL = mTopLineL = screenWidth - 30;
                        mBottomLineT = mTopLineT = actionDownPoint.y;
                        float topLineDegree = (float) (((degree) * Math.PI) / 180);
                        float topDegreeCos = (float) Math.cos(topLineDegree);
                        float topDegreeSin = (float) Math.sin(topLineDegree);
                        mTopLineR = -20 * (topDegreeSin) + (screenWidth - 30);
                        mTopLineB = 20 * (topDegreeCos) + event.getY();
                        //计算下半折线坐标
                        float bottomLineDegree = (float) (((-degree + 180) * Math.PI) / 180);
                        float bottomDegreeCos = (float) Math.cos(bottomLineDegree);
                        float bottomDegreeSin = (float) Math.sin(bottomLineDegree);
                        mBottomLineR = -20 * bottomDegreeSin + (screenWidth - 30);
                        mBottomLineB = 20 * bottomDegreeCos + event.getY();
                    } else {
                        //计算曲线的变化
                        mPathPaint.setAlpha((int)(stretchDegree * 0.8 * 255));
                        bezierCurve.setControlPoint(new PointF(screenWidth - (stretchDegree * 150), actionDownPoint.y));
                        //计算横线的变化
                        mLinePaint.setColor(Color.argb((int)(stretchDegree * 255), 255, 255, 255));
                        float slideRangeX = screenWidth - (stretchDegree * 30);
                        float slideRangeY = stretchDegree * 20;
                        mTransverseLineL = mTransverseLineR = slideRangeX;
                        mTransverseLineT = actionDownPoint.y + slideRangeY;
                        mTransverseLineB = actionDownPoint.y - slideRangeY;
                        //归零折现的变化
                        mTopLineL = mTopLineT = mTopLineR = mTopLineB = 0;
                        mBottomLineL = mBottomLineT = mBottomLineR = mBottomLineB = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //还原二次贝塞尔曲线三个点
                bezierCurve.setStartPoint(new PointF(0, 0));
                bezierCurve.setControlPoint(new PointF(0, 0));
                bezierCurve.setEndPoint(new PointF(0, 0));
                //还原直线与折现的所有点
                mTransverseLineL = mTransverseLineT = mTransverseLineR = mTransverseLineB = 0;
                mBottomLineL = mBottomLineT = mBottomLineR = mBottomLineB = 0;
                mTopLineL = mTopLineT = mTopLineR = mTopLineB = 0;
                //触发事件
                if (leftSlide) {
                    float moveRange = actionDownPoint.x - screenWidth / 5;
                    float moveDegree = moveRange / (screenWidth / 5);
                    if (moveDegree > 1) {
                        mSlideEventListener.leftEvent();
                    }
                }
                if (rightSlide) {
                    float moveRange = (screenWidth - actionDownPoint.x) - screenWidth / 5;
                    float moveDegree = moveRange / (screenWidth / 5);
                    if (moveDegree > 1) {
                        mSlideEventListener.rightEvent();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void highOrderBezierHandleEvent(MotionEvent event) {
        HighOrderBezierCurve bezierCurve = (HighOrderBezierCurve) mBezierCurve;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                actionDownPoint = new PointF(event.getX(), event.getY());

                if (leftSlide && mAllowLeft) {
                    bezierCurve.setFirstStartPoint(new PointF(0, actionDownPoint.y - 300));
                    bezierCurve.setFirstControlPoint(new PointF(0, actionDownPoint.y - 200));
                    bezierCurve.setThirdControlPoint(new PointF(0, actionDownPoint.y + 200));
                    bezierCurve.setThirdEndPoint(new PointF(0, actionDownPoint.y + 300));
                    float stretchDegree = (event.getX() * 5) / screenWidth;
                    if (event.getX() >= screenWidth / 5) {
                        //贝塞尔曲线控制点不再增长
                        mPathPaint.setAlpha((int) (0.8 * 255));
                        bezierCurve.setFirstEndPoint(new PointF(150/3, actionDownPoint.y - 100));
                        bezierCurve.setSecondControlPoint(new PointF(150 / 13 * 10, actionDownPoint.y));
                        bezierCurve.setSecondEndPoint(new PointF(150/3, actionDownPoint.y + 100));
                        //直线转为折现 计算折现角度
                        float moveRange = actionDownPoint.x - screenWidth / 5;
                        float moveDegree = moveRange / (screenWidth / 5);
                        int degree;
                        if (moveDegree >= 1) {
                            degree = 45;
                        } else {
                            degree = (int) (moveDegree * 45);
                        }
                        //修改画笔颜色 去除直线痕迹
                        mLinePaint.setColor(Color.argb(255, 255, 255, 255));
                        mTransverseLineL = mTransverseLineR = mTransverseLineT = mTransverseLineB = 0;
                        //计算上半折线坐标
                        mBottomLineL = mTopLineL = 30;
                        mBottomLineT = mTopLineT = actionDownPoint.y;
                        float topLineDegree = (float) (((-degree) * Math.PI) / 180);
                        float topDegreeCos = (float) Math.cos(topLineDegree);
                        float topDegreeSin = (float) Math.sin(topLineDegree);
                        mTopLineR = -20 * (topDegreeSin) + 30;
                        mTopLineB = 20 * (topDegreeCos) + event.getY();
                        //计算下半折线坐标
                        float bottomLineDegree = (float) (((degree + 180) * Math.PI) / 180);
                        float bottomDegreeCos = (float) Math.cos(bottomLineDegree);
                        float bottomDegreeSin = (float) Math.sin(bottomLineDegree);
                        mBottomLineR = -20 * bottomDegreeSin + 30;
                        mBottomLineB = 20 * bottomDegreeCos + event.getY();
                    } else {
                        //计算曲线的变化
                        mPathPaint.setAlpha((int)(stretchDegree * 0.8 * 255));
                        bezierCurve.setFirstEndPoint(new PointF(stretchDegree * 150/3, actionDownPoint.y - 100));
                        bezierCurve.setSecondControlPoint(new PointF(stretchDegree * 150 / 13 * 10, actionDownPoint.y));
                        bezierCurve.setSecondEndPoint(new PointF(stretchDegree * 150/3, actionDownPoint.y + 100));
                        //计算横线的变化
                        mLinePaint.setColor(Color.argb((int)(stretchDegree * 255), 255, 255, 255));
                        float slideRangeX = stretchDegree * 30;
                        float slideRangeY = stretchDegree * 20;
                        mTransverseLineL = mTransverseLineR = slideRangeX;
                        mTransverseLineT = actionDownPoint.y + slideRangeY;
                        mTransverseLineB = actionDownPoint.y - slideRangeY;
                        //归零折现的变化
                        mTopLineL = mTopLineT = mTopLineR = mTopLineB = 0;
                        mBottomLineL = mBottomLineT = mBottomLineR = mBottomLineB = 0;
                    }
                }

                if (rightSlide && mAllowRight) {
                    bezierCurve.setFirstStartPoint(new PointF(screenWidth, actionDownPoint.y - 300));
                    bezierCurve.setFirstControlPoint(new PointF(screenWidth, actionDownPoint.y - 200));
                    bezierCurve.setThirdControlPoint(new PointF(screenWidth, actionDownPoint.y + 200));
                    bezierCurve.setThirdEndPoint(new PointF(screenWidth, actionDownPoint.y + 300));
                    float stretchDegree = ((screenWidth - event.getX()) * 5) / screenWidth;
                    if (screenWidth - event.getX() >= screenWidth / 5) {
                        //贝塞尔曲线控制点不再增长
                        mPathPaint.setAlpha((int)(0.8 * 255));
                        bezierCurve.setFirstEndPoint(new PointF(screenWidth - (150/3), actionDownPoint.y - 100));
                        bezierCurve.setSecondControlPoint(new PointF(screenWidth - (150 / 13 * 10), actionDownPoint.y));
                        bezierCurve.setSecondEndPoint(new PointF(screenWidth - (150/3), actionDownPoint.y + 100));
                        //直线转为折现 计算折现角度
                        float moveRange = (screenWidth - actionDownPoint.x) - screenWidth / 5;
                        float moveDegree = moveRange / (screenWidth / 5);
                        int degree;
                        if (moveDegree >= 1) {
                            degree = 45;
                        } else {
                            degree = (int) (moveDegree * 45);
                        }
                        //修改画笔颜色 去除直线痕迹
                        mLinePaint.setColor(Color.argb(255, 255, 255, 255));
                        mTransverseLineL = mTransverseLineR = mTransverseLineT = mTransverseLineB = 0;
                        //计算上半折线坐标
                        mBottomLineL = mTopLineL = screenWidth - 30;
                        mBottomLineT = mTopLineT = actionDownPoint.y;
                        float topLineDegree = (float) (((degree) * Math.PI) / 180);
                        float topDegreeCos = (float) Math.cos(topLineDegree);
                        float topDegreeSin = (float) Math.sin(topLineDegree);
                        mTopLineR = -20 * (topDegreeSin) + (screenWidth - 30);
                        mTopLineB = 20 * (topDegreeCos) + event.getY();
                        //计算下半折线坐标
                        float bottomLineDegree = (float) (((-degree + 180) * Math.PI) / 180);
                        float bottomDegreeCos = (float) Math.cos(bottomLineDegree);
                        float bottomDegreeSin = (float) Math.sin(bottomLineDegree);
                        mBottomLineR = -20 * bottomDegreeSin + (screenWidth - 30);
                        mBottomLineB = 20 * bottomDegreeCos + event.getY();
                    } else {
                        //计算曲线的变化
                        mPathPaint.setAlpha((int)(stretchDegree * 0.8 * 255));
                        bezierCurve.setFirstEndPoint(new PointF(screenWidth - (stretchDegree * 150/3), actionDownPoint.y - 100));
                        bezierCurve.setSecondControlPoint(new PointF(screenWidth - (stretchDegree * 150 / 13 * 10), actionDownPoint.y));
                        bezierCurve.setSecondEndPoint(new PointF(screenWidth - (stretchDegree * 150/3), actionDownPoint.y + 100));
                        //计算横线的变化
                        mLinePaint.setColor(Color.argb((int)(stretchDegree * 255), 255, 255, 255));
                        float slideRangeX = screenWidth - (stretchDegree * 30);
                        float slideRangeY = stretchDegree * 20;
                        mTransverseLineL = mTransverseLineR = slideRangeX;
                        mTransverseLineT = actionDownPoint.y + slideRangeY;
                        mTransverseLineB = actionDownPoint.y - slideRangeY;
                        //归零折现的变化
                        mTopLineL = mTopLineT = mTopLineR = mTopLineB = 0;
                        mBottomLineL = mBottomLineT = mBottomLineR = mBottomLineB = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //还原贝塞尔曲线的控制点
                bezierCurve.setFirstStartPoint(new PointF(0, 0));
                bezierCurve.setFirstControlPoint(new PointF(0, 0));
                bezierCurve.setFirstEndPoint(new PointF(0, 0));
                bezierCurve.setSecondControlPoint(new PointF(0, 0));
                bezierCurve.setSecondEndPoint(new PointF(0, 0));
                bezierCurve.setThirdControlPoint(new PointF(0, 0));
                bezierCurve.setThirdEndPoint(new PointF(0, 0));
                //还原直线与折现的所有点
                mTransverseLineL = mTransverseLineT = mTransverseLineR = mTransverseLineB = 0;
                mBottomLineL = mBottomLineT = mBottomLineR = mBottomLineB = 0;
                mTopLineL = mTopLineT = mTopLineR = mTopLineB = 0;
                //触发事件
                if (leftSlide) {
                    float moveRange = actionDownPoint.x - screenWidth / 5;
                    float moveDegree = moveRange / (screenWidth / 5);
                    if (moveDegree > 1) {
                        mSlideEventListener.leftEvent();
                    }
                }
                if (rightSlide) {
                    float moveRange = (screenWidth - actionDownPoint.x) - screenWidth / 5;
                    float moveDegree = moveRange / (screenWidth / 5);
                    if (moveDegree > 1) {
                        mSlideEventListener.rightEvent();
                    }
                }
                break;
            default:
                break;
        }
    }

    public boolean checkIsQuadratic() {
        return mBezierType == BezierCurve.QUADRATIC;
    }

    public void setSlideListener(SlideEventListener listener) {
        mSlideEventListener = listener;
    }

}
