package nl.sebastiaanschool.contact.app;

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
public abstract class HorizontalSlidingFragment extends Fragment {
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HorizontalSlidingLayout slidingContainer = new HorizontalSlidingLayout(inflater.getContext());
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
    public abstract View onCreateView2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

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
}
