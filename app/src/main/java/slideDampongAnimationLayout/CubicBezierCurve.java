package slideDampongAnimationLayout;

import android.graphics.PointF;

/**
 * Copyright (C) 2018 Unicorn, Inc.
 * Description :
 * Created by dabutaizha on 2018/7/30 上午9:33.
 */

public class CubicBezierCurve implements BezierCurve {

    private PointF mStartPoint;
    private PointF mEndPoint;
    private PointF mFirstControlPoint;
    private PointF mSecondControlPoint;

    public CubicBezierCurve() {
        mStartPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mFirstControlPoint = new PointF(0, 0);
        mSecondControlPoint = new PointF(0, 0);
    }

    public PointF getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(PointF mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public PointF getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(PointF mEndPoint) {
        this.mEndPoint = mEndPoint;
    }

    public PointF getFirstControlPoint() {
        return mFirstControlPoint;
    }

    public void setFirstControlPoint(PointF mFirstControlPoint) {
        this.mFirstControlPoint = mFirstControlPoint;
    }

    public PointF getSecondControlPoint() {
        return mSecondControlPoint;
    }

    public void setSecondControlPoint(PointF mSecondControlPoint) {
        this.mSecondControlPoint = mSecondControlPoint;
    }

    @Override
    public String toString() {
        return "CubicBezierCurve{" +
                "mStartPoint=" + mStartPoint +
                ", mEndPoint=" + mEndPoint +
                ", mFirstControlPoint=" + mFirstControlPoint +
                ", mSecondControlPoint=" + mSecondControlPoint +
                '}';
    }

}
