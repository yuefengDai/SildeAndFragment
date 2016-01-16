package com.example.administrator.sildeandfragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/1/14.
 */
public class MyFrameLayout extends FrameLayout {

    private View contentView;
    private View tetileView;
    private int measuredHeight;
    private int tetileWidth;

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller=new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        tetileView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight =tetileView.getMeasuredHeight();
        tetileWidth = tetileView.getWidth();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        tetileView.layout(-tetileWidth,0,0,measuredHeight);
    }
    private int lastx,downx;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                lastx=downx=rawX;
                break;
            case MotionEvent.ACTION_MOVE :
                int dx = rawX - lastx;
                int scrollX=getScrollX()-dx;
                //-tetileWidth--->0
                if(scrollX>0) {
                    scrollX=0;
                }
                if(scrollX<-tetileWidth) {
                    scrollX=-tetileWidth;
                }
                scrollTo(scrollX,getScrollY());
                lastx=rawX;
                //invalidate();
                break;
            case MotionEvent.ACTION_UP :
                int total =getScrollX();//获取偏移量
                if(total>-tetileWidth/2) {
                    close();
                }else {
                    open();
                }
                break;
        }

        return true;
    }

    private void close() {//0--->-tetilewidth
        scroller.startScroll(getScrollX(),getScrollY(),-getScrollX(),0);
        invalidate();
    }

    private void open() {//-tetilewidth---->0
        scroller.startScroll(getScrollX(),getScrollY(),-tetileWidth-getScrollX(),0);
        invalidate();
    }

    private Scroller scroller;

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
}
