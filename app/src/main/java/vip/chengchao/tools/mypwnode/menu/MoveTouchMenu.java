package vip.chengchao.tools.mypwnode.menu;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import vip.chengchao.tools.mypwnode.R;


/**
 * Created by chengchao on 16/7/3.
 */
public class MoveTouchMenu {
    private static final String TAG = "MoveTouchMenu";
    private static final int MIN_MOVED_DIST = 5;
    private boolean destroyed = false;
    private Context context;

    private WindowManager.LayoutParams layoutParams;
    private WindowManager manager;
    private View touchView;
    private View menuView;
    private DisplayMetrics metrics;
    private int showX = 100;
    private int showY = 100;
    private int showWidth = WindowManager.LayoutParams.WRAP_CONTENT;
    private int showHeight = WindowManager.LayoutParams.WRAP_CONTENT;

    public MoveTouchMenu(Context context) {
        this.context = context;
        this.metrics = context.getResources().getDisplayMetrics();
    }

    public void show() {
        manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        layoutParams = buildLayoutParams(showX, showY, showWidth, showHeight);

        touchView.setOnTouchListener(touchListener);
        touchView.setOnClickListener(onClickListener);
        manager.addView(touchView, layoutParams);
    }

    public MoveTouchMenu setBottom(int b) {
        showY = metrics.heightPixels - b;
        return this;
    }

    public MoveTouchMenu setRight(int r) {
        showX = metrics.widthPixels - r;
        return this;
    }

    public MoveTouchMenu setTop(int t) {
        showY = t;
        return this;
    }

    public MoveTouchMenu setLeft(int l) {
        showX = l;
        return this;
    }

    public MoveTouchMenu setXY(int x, int y) {
        this.showX = x;
        this.showY = y;
        return this;
    }

    public MoveTouchMenu setWidth(int width) {
        this.showWidth = width;
        return this;
    }

    public MoveTouchMenu setHeight(int height) {
        this.showHeight = height;
        return this;
    }

    public MoveTouchMenu setTouchView(View view) {
        this.touchView = view;
        return this;
    }

    public MoveTouchMenu setTouchView(int resource) {
        touchView = LayoutInflater.from(context).inflate(R.layout.menu_flag, null);
        return this;
    }

    private WindowManager.LayoutParams buildLayoutParams(int x, int y, int width, int height) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //这个参数会造成弹窗浮层无法获得焦点,即edittext无法输入文字,如果不加会导致添加的View在焦点,不论点哪都能触发touch事件
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.x = x;
        layoutParams.y = y;
        layoutParams.width = width;
        layoutParams.height = height;
        return layoutParams;
    }

    public MoveTouchMenu addMenuView(View view) {
        this.menuView = view;
        return this;
    }

    public MoveTouchMenu removeMenuView() {
        if (menuView != null) {
            manager.removeView(menuView);
        }
        return this;
    }

    public void destroy() {
        if (touchView != null && touchView.isShown()) {
            manager.removeView(touchView);
        }
        if (menuView != null && menuView.isShown()) {
            manager.removeView(menuView);
        }
        layoutParams = null;
        touchView = null;
        menuView = null;
        manager = null;
        context = null;
        metrics = null;
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        private float touchDownX;
        private float touchDownY;
        private int paramX;
        private int paramY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchDownX = motionEvent.getRawX();
                    touchDownY = motionEvent.getRawY();
                    paramX = layoutParams.x;
                    paramY = layoutParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) (motionEvent.getRawX() - touchDownX);
                    int dy = (int) (motionEvent.getRawY() - touchDownY);
                    int left = view.getLeft() + dx;
                    int right = view.getRight() + dx;
                    int bottom = view.getBottom() + dy;
                    int top = view.getTop() + dy;
                    if (left < 20) {
                        left = 0;
                        right = left + view.getWidth();
                    }
                    if (right > metrics.widthPixels - 20) {
                        right = metrics.widthPixels;
                        left = right - view.getWidth();
                    }
                    if (top < 20) {
                        top = 0;
                        bottom = top + view.getHeight();
                    }
                    if (bottom > metrics.heightPixels - 20) {
                        bottom = metrics.heightPixels;
                        top = bottom - view.getHeight();
                    }
                    //用于Activity中移动使用
//                view.layout(left, top, right, bottom);
                    layoutParams.x = paramX + dx;
                    layoutParams.y = paramY + dy;

                    manager.updateViewLayout(view, layoutParams);
//                touchDownX = motionEvent.getRawX();
//                touchDownY = motionEvent.getRawY();
//                view.postInvalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    dx = (int) (motionEvent.getRawX() - touchDownX);
                    dy = (int) (motionEvent.getRawY() - touchDownY);
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    return dist >= MIN_MOVED_DIST;
            }
            return false;
        }

    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (menuView != null && !menuView.isShown()) {
                manager.addView(menuView, layoutParams);
            }
        }
    };

    public DisplayMetrics getMetrics() {
        return metrics;
    }
}
