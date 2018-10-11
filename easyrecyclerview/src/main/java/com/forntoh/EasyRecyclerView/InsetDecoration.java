package com.forntoh.EasyRecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class InsetDecoration extends RecyclerView.ItemDecoration {

    private int spacing;
    private int left, right, top, bottom;
    private Sides sides;

    InsetDecoration(Context context, int inset) {
        this.spacing = (int) dpToPixels(context, inset);
    }

    private static float dpToPixels(Context c, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, c.getResources().getDisplayMetrics());
    }

    void applyTo(Sides sides) {
        this.sides = sides;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int size = state.getItemCount();

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int cols = ((GridLayoutManager) layoutManager).getSpanCount();
            left = spacing / 2;
            right = spacing / 2;
            top = position / cols == 0 ? spacing : spacing / 2;
            bottom = spacing / 2;
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                left = position == 0 ? spacing : spacing / 2;
                right = position == size - 1 ? spacing : spacing / 2;
                top = spacing;
                bottom = spacing;
            } else {
                left = spacing;
                right = spacing;
                top = position == 0 ? spacing : spacing / 2;
                bottom = position == size - 1 ? spacing : spacing / 2;
            }
        }

        if (sides == Sides.TOP_BOTTOM) {
            left = 0;
            right = 0;
        } else if (sides == Sides.LEFT_RIGHT) {
            top = 0;
            bottom = 0;
        }

        outRect.left = left;
        outRect.right = right;
        outRect.top = top;
        outRect.bottom = bottom;
    }

    public enum Sides {
        TOP_BOTTOM, LEFT_RIGHT
    }
}
