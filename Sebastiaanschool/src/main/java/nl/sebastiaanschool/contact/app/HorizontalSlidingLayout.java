package nl.sebastiaanschool.contact.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * View root for detail fragments in this application; contains stuff to facilitate a horizontally
 * sliding entry/exit animation.
 * <p/>
 * This class shouldn't exist. Seriously. Horrible horrible horrible API fail. Simple things should
 * be simple. Details in http://www.youtube.com/watch?v=xbl5cxfA1n4
 */
public class HorizontalSlidingLayout extends FrameLayout {
    private int screenWidth;
    private float horizontalOffsetFraction = 1.0f; // TODO make initial value an attribute.

    public HorizontalSlidingLayout(Context context) {
        super(context);
    }

    public HorizontalSlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setX(horizontalOffsetFraction > 0 ? horizontalOffsetFraction * w : 0);
        screenWidth = w;
    }

    public float getHorizontalOffsetFraction() {
        return horizontalOffsetFraction;
    }

    @SuppressWarnings("unused") // Invoked via reflection by an animator.
    public void setHorizontalOffsetFraction(float fraction) {
        this.horizontalOffsetFraction = fraction;
        setX(fraction > 0 ? fraction * screenWidth : 0);
    }
}
