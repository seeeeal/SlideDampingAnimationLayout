package slideDampongAnimationLayout;

import android.graphics.PointF;

/**
 * Copyright (C) 2018 Unicorn, Inc.
 * Description :
 * Created by dabutaizha on 2018/8/27 下午2:12.
 */

public class HighOrderBezierCurve implements BezierCurve {

    private PointF mFirstStartPoint;
    private PointF mFirstControlPoint;
    private PointF mFirstEndPoint;
    private PointF mSecondStartPoint;
    private PointF mSecondControlPoint;
    private PointF mSecondEndPoint;
    private PointF mThirdStartPoint;
    private PointF mThirdControlPoint;
    private PointF mThirdEndPoint;

    public HighOrderBezierCurve() {
        this.mFirstStartPoint = new PointF(0, 0);
        this.mFirstControlPoint = new PointF(0, 0);
        this.mFirstEndPoint = new PointF(0, 0);
        this.mSecondStartPoint = new PointF(0, 0);
        this.mSecondControlPoint = new PointF(0, 0);
        this.mSecondEndPoint = new PointF(0, 0);
        this.mThirdStartPoint = new PointF(0, 0);
        this.mThirdControlPoint = new PointF(0, 0);
        this.mThirdEndPoint = new PointF(0, 0);
    }

    public PointF getFirstStartPoint() {
        return mFirstStartPoint;
    }

    public void setFirstStartPoint(PointF firstStartPoint) {
        this.mFirstStartPoint = firstStartPoint;
    }

    public PointF getFirstControlPoint() {
        return mFirstControlPoint;
    }

    public void setFirstControlPoint(PointF firstControlAPoint) {
        this.mFirstControlPoint = firstControlAPoint;
    }

    public PointF getFirstEndPoint() {
        return mFirstEndPoint;
    }

    public void setFirstEndPoint(PointF firstEndAPoint) {
        this.mFirstEndPoint = firstEndAPoint;
    }

    public PointF getSecondStartPoint() {
        return mSecondStartPoint;
    }

    public void setSecondStartPoint(PointF secondStartPoint) {
        this.mSecondStartPoint = secondStartPoint;
    }

    public PointF getSecondControlPoint() {
        return mSecondControlPoint;
    }

    public void setSecondControlPoint(PointF secondControlAPoint) {
        this.mSecondControlPoint = secondControlAPoint;
    }

    public PointF getSecondEndPoint() {
        return mSecondEndPoint;
    }

    public void setSecondEndPoint(PointF secondEndAPoint) {
        this.mSecondEndPoint = secondEndAPoint;
    }

    public PointF getThirdStartPoint() {
        return mThirdStartPoint;
    }

    public void setThirdStartPoint(PointF thirdStartPoint) {
        this.mThirdStartPoint = thirdStartPoint;
    }

    public PointF getThirdControlPoint() {
        return mThirdControlPoint;
    }

    public void setThirdControlPoint(PointF thirdControlAPoint) {
        this.mThirdControlPoint = thirdControlAPoint;
    }

    public PointF getThirdEndPoint() {
        return mThirdEndPoint;
    }

    public void setThirdEndPoint(PointF thirdEndAPoint) {
        this.mThirdEndPoint = thirdEndAPoint;
    }

    @Override
    public String toString() {
        return "HighOrderBezierCurve{" +
                "mFirstStartPoint=" + mFirstStartPoint +
                ", mFirstControlPoint=" + mFirstControlPoint +
                ", mFirstEndPoint=" + mFirstEndPoint +
                ", mSecondStartPoint=" + mSecondStartPoint +
                ", mSecondControlPoint=" + mSecondControlPoint +
                ", mSecondEndPoint=" + mSecondEndPoint +
                ", mThirdStartPoint=" + mThirdStartPoint +
                ", mThirdControlPoint=" + mThirdControlPoint +
                ", mThirdEndPoint=" + mThirdEndPoint +
                '}';
    }
}
