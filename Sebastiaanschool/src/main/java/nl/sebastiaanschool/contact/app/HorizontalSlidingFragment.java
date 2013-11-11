/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Base class for fragments that slide into the screen from right to left.
 * Created by Barend on 2-11-13.
 */
public abstract class HorizontalSlidingFragment extends Fragment implements Animator.AnimatorListener {

    private Callback callback;
    private HorizontalSlidingLayout slidingContainer;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slidingContainer = new HorizontalSlidingLayout(inflater.getContext());
        View childView = onCreateView2(inflater, slidingContainer, savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        int id = enter? R.animator.hsl_slide_in : R.animator.hsl_slide_out;
        Animator animator = AnimatorInflater.loadAnimator(getActivity(), id);
        if (animator != null) {
            animator.addListener(this);
        }
        return animator;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        if (callback != null) {
            boolean willOpen = slidingContainer.getPercentOnScreen() <= 0.0f;
            callback.onSlidingFragmentBeginAnimation(this, willOpen);
        }
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (callback != null) {
            boolean hasOpened = slidingContainer.getPercentOnScreen() >= 1.0f;
            callback.onSlidingFragmentEndAnimation(this, hasOpened);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {
        // Ignored
    }

    @Override
    public void onAnimationRepeat(Animator animator) {
        // Ignored
    }

    public interface Callback {
        void onSlidingFragmentBeginAnimation(HorizontalSlidingFragment source, boolean willSlideIntoView);
        void onSlidingFragmentEndAnimation(HorizontalSlidingFragment source, boolean didSlideIntoView);
    }
}
