package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
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
        init();
    }

    public HorizontalSlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            // Ensure content is visible in edit mode.
            percentOnScreen = 1.0f;
        } else {
            setWillNotDraw(false);
            percentOnScreen = 0.0f;
            shadowPaint = new Paint();
            shadowPaint.setAlpha(0x00);
            shadowPaint.setColor(0x000000);
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

    @SuppressWarnings("unused")
    public void setPercentOnScreen(float fraction) {
        this.percentOnScreen = fraction;
        shift = (int)(fraction < 1.0 ? fraction * screenWidth : screenWidth);
        setX(-shift);
        invalidate(shift, 0, screenWidth, screenHeight);
    }
}
