package slideDampongAnimationLayout;

import android.graphics.PointF;

/**
 * Copyright (C) 2018 Unicorn, Inc.
 * Description :
 * Created by dabutaizha on 2018/7/30 上午9:29.
 */

public class QuadraticBezierCurve implements BezierCurve {

    private PointF mStartPoint;
    private PointF mEndPoint;
    private PointF mControlPoint;

    public QuadraticBezierCurve() {
        mStartPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mControlPoint = new PointF(0, 0);
    }

    public PointF getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(PointF startPoint) {
        this.mStartPoint = startPoint;
    }

    public PointF getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(PointF endPoint) {
        this.mEndPoint = endPoint;
    }

    public PointF getControlPoint() {
        return mControlPoint;
    }

    public void setControlPoint(PointF controlPoint) {
        this.mControlPoint = controlPoint;
    }

    @Override
    public String toString() {
        return "QuadraticBezierCurve{" +
                "startPoint=" + mStartPoint +
                ", endPoint=" + mEndPoint +
                ", controlPoint=" + mControlPoint +
                '}';
    }

}
