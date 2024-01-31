package com.emt.pdgo.next.util.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 文件说明
 *
 * @author chenjh
 * @date 2019/2/14 10:36
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int left;
    private int right;

    public SpaceItemDecoration(int space) {
        this.space = space;
        this.left = space;
        this.right = space;
    }

    public SpaceItemDecoration(int space, int left, int right) {
        this.space = space;
        this.left = left;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }

}
