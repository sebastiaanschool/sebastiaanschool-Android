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

    private final int topMarginNextItem;
    private final int horizontalMargin;

    public CardMarginsDecorator(Context context) {
        this.topMarginNextItem = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_top_next);
        this.horizontalMargin = context.getResources().getDimensionPixelOffset(R.dimen.card_margin_horizontal);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = topMarginNextItem;
        outRect.left = horizontalMargin;
        outRect.right = horizontalMargin;
    }
}
