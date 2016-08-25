package nl.sebastiaanschool.contact.app.gui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import nl.sebastiaanschool.contact.app.R;

/**
 * Adds a bit of tasteful margin around the RecyclerView cards.
 */
class CardMarginsDecorator extends RecyclerView.ItemDecoration {

    private final int topMarginFirstItem;
    private final int topMarginNextItem;
    private final int bottomMarginLastItem;
    private final int horizontalMargin;

    public CardMarginsDecorator(Context context) {
        this.topMarginFirstItem = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_top_first);
        this.topMarginNextItem = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_top_next);
        this.bottomMarginLastItem = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_bottom_last);
        this.horizontalMargin = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_horizontal);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int pos = parent.getChildLayoutPosition(view);
        if (pos == 0) {
            outRect.top = topMarginFirstItem;
        } else {
            outRect.top = topMarginNextItem;
        }
        if (pos == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomMarginLastItem;
        }
        outRect.left = horizontalMargin;
        outRect.right = horizontalMargin;
    }
}
