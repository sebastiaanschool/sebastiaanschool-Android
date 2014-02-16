/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
package nl.sebastiaanschool.contact.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.concurrent.TimeUnit;

/**
 * Base class for fragments that slide into the screen from right to left.
 * Created by Barend on 2-11-13.
 */
public abstract class HorizontalSlidingFragment extends Fragment implements Animator.AnimatorListener, View.OnTouchListener, GestureDetector.OnGestureListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    /**
     * If the animation lasted shorter than this, assume that animations have been disabled in the developer options.
     */
    private static final long MIN_ANIM_DURATION_NANOS = TimeUnit.MILLISECONDS.toNanos(100L);
    private Callback callback;
    private HorizontalSlidingLayout slidingContainer;
    private long animStartNanos;
    private boolean animEntering;
    private GestureDetector gestureDetector;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gestureDetector = new GestureDetector(inflater.getContext(), this);
        slidingContainer = new HorizontalSlidingLayout(inflater.getContext());
        View childView = onCreateView2(inflater, slidingContainer, savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        childView.setOnTouchListener(this);
        slidingContainer.addView(childView, params);
        return slidingContainer;
    }

    /**
     * Subclasses must use this method in place of {@code #onCreateView(...)}.
     * @param inflater non-null.
     * @param container non-null.
     * @param savedInstanceState nullable.
     * @return the subclass' content view.
     */
    protected abstract View onCreateView2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * Configures a fragment transaction to make the sliding appearance work.
     * @param tx a fragment transaction (required).
     * @param containerResId the container view id for this fragment's content (required).
     * @param backStackLabel the label by which to add this fragment to the back stack (nullable).
     * @return the same object as {@code tx}, for method chaining.
     */
    public FragmentTransaction addWithAnimation(FragmentTransaction tx, int containerResId, String backStackLabel) {
        return tx.addToBackStack(backStackLabel)
                .setCustomAnimations(R.animator.hsl_slide_in, 0, 0, R.animator.hsl_slide_out)
                .add(containerResId, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            this.callback = (Callback) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        animEntering = enter;
        int id = enter? R.animator.hsl_slide_in : R.animator.hsl_slide_out;
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), id);
        if (animator != null) {
            animator.addListener(this);
        }
        return animator;
    }

    /** @category AnimatorListener */
    @Override
    public void onAnimationStart(Animator animator) {
        animStartNanos = System.nanoTime();
        if (callback != null) {
            boolean willOpen = slidingContainer.getPercentOnScreen() <= 0.0f;
            callback.onSlidingFragmentBeginAnimation(this, animEntering);
        }
    }

    /** @category AnimatorListener */
    @Override
    public void onAnimationEnd(Animator animator) {
        long elapsedNanos = System.nanoTime() - animStartNanos;
        if (elapsedNanos < MIN_ANIM_DURATION_NANOS) {
            if (slidingContainer.getMeasuredWidth() == 0) {
                slidingContainer.requestLayout();
            }
            slidingContainer.setPercentOnScreen(animEntering ? 1.0f : 0.0f);
        }
        if (callback != null) {
            callback.onSlidingFragmentEndAnimation(this, animEntering);
        }
    }

    /**
     * Returns the string resource ID to show in the ActionBar Subtitle when navigating to this fragment.
     * @return defaults to {@code 0}, for no subtitle.
     */
    public int getTitleResId() {
        return 0;
    }

    /**
     * Returns the string resource ID for an accessibility announcement to be spoken when navigating to this fragment.
     * @return defaults to {@code R.string.accessibility__announce_open_page}.
     */
    public int getAnnouncementResId() {
        return R.string.accessibility__announce_open_page;
    }

    /** @category AnimatorListener */
    @Override
    public void onAnimationCancel(Animator animator) {
        // Ignored
    }

    /** @category AnimatorListener */
    @Override
    public void onAnimationRepeat(Animator animator) {
        // Ignored
    }

    /** @category OnTouchListener */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * Adapted from http://stackoverflow.com/a/12938787/49489.
     * @category OnGestureListener
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        this.callback.onSlidingFragmentBackGesture();
                    }
                }
            }
        } catch (Exception exception) {
            // Ignored
        }
        return result;
    }

    /** @category OnGestureListener */
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    /** @category OnGestureListener */
    @Override
    public void onShowPress(MotionEvent e) { }

    /** @category OnGestureListener */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /** @category OnGestureListener */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    /** @category OnGestureListener */
    @Override
    public void onLongPress(MotionEvent e) { }

    /**
     * Activities hosting this fragment must implement this interface.
     */
    public interface Callback {
        void onSlidingFragmentBackGesture();
        void onSlidingFragmentBeginAnimation(HorizontalSlidingFragment source, boolean willSlideIntoView);
        void onSlidingFragmentEndAnimation(HorizontalSlidingFragment source, boolean didSlideIntoView);
    }
}
