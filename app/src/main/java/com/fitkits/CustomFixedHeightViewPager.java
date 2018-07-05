package com.fitkits;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomFixedHeightViewPager extends android.support.v4.view.ViewPager {
    private boolean enabled;

    /**
     * Class Constructor assigning values for context and attribute set.
     * @param context Context of the view.
     * @param attrs Attribute set of the view.
     */
    public CustomFixedHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled ? super.onTouchEvent(event) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled ? super.onInterceptTouchEvent(event) : false;
    }

    /**
     * Checks if paging is enabled.
     * @return boolean with respect to page enabled/disabled.
     */
    public boolean isPagingEnabled() {
        return enabled;
    }

    /**
     * Sets paging as enabled.
     * @param enabled boolean to determine if paging is enabled or disabled.
     */
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}