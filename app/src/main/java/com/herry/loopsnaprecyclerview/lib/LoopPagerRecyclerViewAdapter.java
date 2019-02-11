package com.herry.loopsnaprecyclerview.lib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class LoopPagerRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, VM> extends RecyclerView.Adapter<VH> {
    private int maxItemCounts = 0;

    @NonNull
    private final List<VM> items = new ArrayList<>();

    private int defaultPosition = RecyclerView.NO_POSITION;

    public final void setItems(@NonNull Collection<VM> items) {
        this.items.clear();
        this.items.addAll(items);

        notifyDataSetChanged();

        // sets default position
        int itemCounts = this.items.size();
        if (1 < itemCounts) {
            maxItemCounts = Integer.MAX_VALUE;
            defaultPosition = (maxItemCounts / 2) - ((maxItemCounts / 2) % this.items.size());
        } else if (0 < itemCounts) {
            maxItemCounts = itemCounts;
            defaultPosition = 0;
        } else {
            maxItemCounts = this.items.size();
        }
    }

    protected final VM getItem(int position) {
        int realPosition = getRealPosition(position);
        if (0 <= realPosition && realPosition < this.items.size()) {
            return this.items.get(realPosition);
        }

        return null;
    }

    @Override
    public final int getItemCount() {
        return maxItemCounts;
    }

    public int getRealItemCount() {
        return this.items.size();
    }

    int getRealPosition(int fakePosition) {
        return 1 < this.items.size() ? fakePosition % items.size() : fakePosition;
    }

    int getFakePosition(int realPosition) {
        if (0 <= realPosition && realPosition < this.items.size()) {
            return defaultPosition + realPosition;
        }

        return RecyclerView.NO_POSITION;
    }
}
