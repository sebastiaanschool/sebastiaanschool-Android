/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * View root for detail fragments in this application; contains stuff to facilitate a horizontally
 * sliding entry/exit animation.
 * <p/>
 * This class shouldn't exist. Seriously. Horrible horrible horrible API fail. Simple things should
 * be simple. Details in http://www.youtube.com/watch?v=xbl5cxfA1n4
 */
public class HorizontalSlidingLayout extends FrameLayout {
    /**
     * The fraction by which the content has slid into view. Legal range: from 0.0 (all content
     * off-screen) to 1.0 (all content visible).
     */
    private float percentOnScreen;
    private int screenWidth, screenHeight, shift;
    private Paint shadowPaint;

    public HorizontalSlidingLayout(Context context) {
        super(context);
        init(context);
    }

    public HorizontalSlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            // Ensure content is visible in edit mode.
            percentOnScreen = 1.0f;
        } else {
            setWillNotDraw(false);
            percentOnScreen = 0.0f;
            shadowPaint = new Paint();
            shadowPaint.setAlpha(0x00);
            shadowPaint.setColor(context.getResources().getColor(R.color.hsl_shadow_color));
            shadowPaint.setStyle(Paint.Style.FILL);
        }
    }

    /** Reports our own size as (2w, h) and measures all children at (w, h). */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(2 * screenWidth, screenHeight);
        for (int i = 0, max = getChildCount(); i < max; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
        // (Re-)initialize this.shift for the new screen dimensions, to make the contents appear
        // correctly if animations are disabled in the Developer Options.
        shift = (int)(percentOnScreen < 1.0 ? percentOnScreen * screenWidth : screenWidth);
        setX(-shift);
    }

    /** Lays out the children in the right half of the view. */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0, max = getChildCount(); i < max; i++) {
            View child = getChildAt(i);
            child.layout(screenWidth, top, right, bottom);
        }
    }

    /**
     * Draws a translucent shadow in the left half of the view, darkening by
     * {@code percentOnScreen}, then lets superclass draw children in the right half.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Maintain 30% translucency
        if (percentOnScreen < 0.7) {
            shadowPaint.setAlpha((int) (percentOnScreen * 0xFF));
        }
        canvas.drawRect(shift, 0, screenWidth, screenHeight, shadowPaint);
        super.onDraw(canvas);
    }

    @SuppressWarnings("unused")
    public float getPercentOnScreen() {
        return percentOnScreen;
    }

    public void setPercentOnScreen(float fraction) {
        shift = (int)(fraction < 1.0 ? fraction * screenWidth : screenWidth);
        setX(-shift);
        invalidate(shift, 0, screenWidth, screenHeight);

        // If we're closing and attached to the view hierarchy, invalidate our parent to work
        // around the issue in http://stackoverflow.com/q/19742350/49489
        if (this.percentOnScreen > fraction && getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).invalidate();
        }
        this.percentOnScreen = fraction;
    }
}
