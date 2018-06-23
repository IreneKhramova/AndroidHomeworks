package com.example.irene.homework4;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        outRect.left = space;
        outRect.right = space;

        RecyclerView.Adapter adapter = parent.getAdapter();
        int position = parent.getChildLayoutPosition(view);
        int type = adapter.getItemViewType(parent.getChildViewHolder(view).getAdapterPosition());


        if (type == CardsAdapter.TYPE_CARD_HALF && (position % 2 == 0)) {
            outRect.right = 0;
        }

        // Add top margin only for the first line to avoid double space between items
        if ((type == CardsAdapter.TYPE_CARD_FULL && position == 0)
                || (type == CardsAdapter.TYPE_CARD_HALF && (position == 0 || position == 1))) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
