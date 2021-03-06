package com.justinchau.snaptrailsandroidmobile;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(
            Rect outRect,
            View view,
            RecyclerView parent,
            RecyclerView.State state
    ) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount;

        if (includeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = spacing - column * spacing / spanCount;
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount;

            // top edge
            if (position < spanCount) {
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = column * spacing / spanCount;
            // spacing = (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = spacing = (column + 1) * spacing / spanCount;

            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}