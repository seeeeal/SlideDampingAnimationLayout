# SlideDampingAnimationLayout
自定义View练手 高仿即刻客户端侧滑回退动画

Gradle
------
```
dependencies {
    ...
    implementation 'com.github.dabutaizha:SlideDampingAnimationLayout:v1.0.2'
}
```

Usage
-----
```xml
<slideDampongAnimationLayout.SlideDampingAnimationLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/search_result_slide_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:bezier_curves_color="@color/black"
    app:bezier_curves_type="quadratic_bezier_curves"
    app:allow_gesture="only_left">
  
    <--content-->
  
</slideDampongAnimationLayout.SlideDampingAnimationLayout>
```
Code
-----
```
mSlideAnimationLayout.setSlideListener(new SlideEventListener() {
            @Override
            public void leftEvent() {
            }

            @Override
            public void rightEvent() {
            }
        });
```


原即刻APP效果

<img src="https://s2.ax1x.com/2019/06/21/Vzygb9.gif" width="300" hegiht="300" alt=“pic0”/>

仿写效果(分为两种 三个控制点贝塞尔曲线和六个控制点绘制的贝塞尔曲线 下图为仿即刻的效果)

<img src="https://s2.ax1x.com/2019/06/21/Vzy338.gif" width="300" hegiht="300" alt=“pic2”/>

### 已实现功能
* 继承FrameLayout 可以左右滑动触发动画与事件
* 可配置是否只触发单一方向动画与事件
* 可配置颜色
* 可选择两种样式的贝塞尔曲线

