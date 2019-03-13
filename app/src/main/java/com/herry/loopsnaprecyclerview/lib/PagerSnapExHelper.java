package com.herry.loopsnaprecyclerview.lib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PagerSnapExHelper extends PagerSnapHelper {
    public interface OnSnappedListener {
        void onSnapped(int position, int itemCount);
        void onUnsnapped(int position, int itemCount);
    }

    private OnSnappedListener onSnappedListener = new OnSnappedListener() {
        @Override
        public void onSnapped(int position, int itemCount) {
        }

        @Override
        public void onUnsnapped(int position, int itemCount) {
        }
    };

    private int snappedPosition = RecyclerView.NO_POSITION;
    private RecyclerView recyclerView = null;

    public PagerSnapExHelper() {
        super();
    }

    public void setOnSnappedListener(OnSnappedListener listener) {
        onSnappedListener = listener;
    }

    @Override
    public int findTargetSnapPosition(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int targetSnapViewPosition = findTargetSnapViewPosition(layoutManager, velocityX, velocityY);

        if (recyclerView != null && recyclerView.getAdapter() != null && targetSnapViewPosition >= 0 && targetSnapViewPosition < recyclerView.getAdapter().getItemCount()) {
            if (null != onSnappedListener) {
                boolean changed = false;
                int unsnappedPosition = -1;
                int snappedPosition = this.snappedPosition;
                if (snappedPosition != targetSnapViewPosition) {
                    unsnappedPosition = this.snappedPosition;
                    snappedPosition = targetSnapViewPosition;
                    changed = true;
                }
                if (changed) {
                    int itemCount = recyclerView.getAdapter().getItemCount();
                    if (0 <= unsnappedPosition) {
                        onSnappedListener.onUnsnapped(unsnappedPosition, itemCount);
                    }

                    this.snappedPosition = snappedPosition;
                    notifySnapped(unsnappedPosition, snappedPosition, itemCount);
                }
            }
        }
        return targetSnapViewPosition;
    }

    protected int findTargetSnapViewPosition(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        this.recyclerView = recyclerView;
        super.attachToRecyclerView(this.recyclerView);
    }

    public void scrollToSnapPosition(int position) {
        if (null == this.recyclerView || null == this.recyclerView.getAdapter()) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = this.recyclerView.getLayoutManager();
        if (null != layoutManager && null != onSnappedListener) {
            if(snappedPosition != position) {
                int itemCount = this.recyclerView.getAdapter().getItemCount();
                layoutManager.scrollToPosition(position);

                int unsnappedPosition = snappedPosition;
                snappedPosition = position;
                notifySnapped(unsnappedPosition, snappedPosition, itemCount);
            }
        }
    }

    public int getCurrentSnappedPosition() {
        return snappedPosition;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    protected OnSnappedListener getOnSnappedListener() {
        return onSnappedListener;
    }

    protected void notifySnapped(int unsnappedPosition, int snappedPosition, int itemCounts) {
        OnSnappedListener onSnappedListener = getOnSnappedListener();
        if (null == onSnappedListener) {
            return;
        }

        if (0 <= unsnappedPosition) {
            onSnappedListener.onUnsnapped(unsnappedPosition, itemCounts);
        }

        if (0 <= snappedPosition) {
            onSnappedListener.onSnapped(snappedPosition, itemCounts);
        }
    }

    public boolean isScrollable() {
        if (null == recyclerView || null == recyclerView.getAdapter()) {
            return false;
        }
        return (1 < recyclerView.getAdapter().getItemCount());
    }

    public void snapToNext() {
        if (isScrollable()) {
            if (null == recyclerView) {
                return;
            }

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null == adapter) {
                return;
            }

            int currentSnappedPosition = snappedPosition;
            if ((RecyclerView.NO_POSITION == snappedPosition) || (currentSnappedPosition >= adapter.getItemCount() - 1)) {
                return;
            }

            snappedPosition = currentSnappedPosition + 1;
            recyclerView.smoothScrollToPosition(snappedPosition);
            notifySnapped(currentSnappedPosition, snappedPosition, adapter.getItemCount());
        }
    }

    public void snapToPrevious() {
        if (isScrollable()) {
            if (null == recyclerView) {
                return;
            }

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (null == adapter) {
                return;
            }

            int currentSnappedPosition = snappedPosition;
            if ((RecyclerView.NO_POSITION == snappedPosition) || (currentSnappedPosition <= 0)) {
                return;
            }

            snappedPosition = currentSnappedPosition - 1;
            recyclerView.smoothScrollToPosition(snappedPosition);
            notifySnapped(currentSnappedPosition, snappedPosition, adapter.getItemCount());
        }
    }
}
