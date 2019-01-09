package com.herry.loopsnaprecyclerview.lib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

@SuppressWarnings("WeakerAccess")
public class LoopPagerSnapExHelper extends PagerSnapExHelper {

    private int snappedRealPosition = RecyclerView.NO_POSITION;
    private int snappedFakeViewPosition = RecyclerView.NO_POSITION;

    @Override
    public int findTargetSnapPosition(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        RecyclerView recyclerView = getRecyclerView();
        if (null == recyclerView) {
            return RecyclerView.NO_POSITION;
        }

        if (!(recyclerView.getAdapter() instanceof LoopPagerRecyclerViewAdapter)) {
            return super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        }

        int targetSnapViewPosition = findTargetSnapViewPosition(layoutManager, velocityX, velocityY);
        if (targetSnapViewPosition >= 0 && targetSnapViewPosition < recyclerView.getAdapter().getItemCount()) {
            int targetSnapRealPosition = ((LoopPagerRecyclerViewAdapter) recyclerView.getAdapter()).getRealPosition(targetSnapViewPosition);
            int totalRealItemCounts = ((LoopPagerRecyclerViewAdapter) recyclerView.getAdapter()).getRealItemCount();

            if (this.snappedRealPosition != targetSnapRealPosition) {
                int unsnappedPosition = this.snappedRealPosition;
                this.snappedRealPosition = targetSnapRealPosition;
                notifySnapped(unsnappedPosition, this.snappedRealPosition, totalRealItemCounts, targetSnapViewPosition);
            }
        }
        return targetSnapViewPosition;
    }

    public void scrollToSnapPosition(int position) {
        RecyclerView recyclerView = getRecyclerView();
        if (null == recyclerView) {
            return;
        }

        if (!(recyclerView.getAdapter() instanceof LoopPagerRecyclerViewAdapter)) {
            super.scrollToSnapPosition(position);
            return;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        OnSnappedListener onSnappedListener = getOnSnappedListener();
        if (null != layoutManager && null != onSnappedListener) {
            if(snappedRealPosition != position) {
                // gets fake position from real position
                int fakePosition = ((LoopPagerRecyclerViewAdapter) recyclerView.getAdapter()).getFakePosition(position);
                if (RecyclerView.NO_POSITION == fakePosition) {
                    return;
                }

                int totalRealItemCounts = ((LoopPagerRecyclerViewAdapter) recyclerView.getAdapter()).getRealItemCount();
                layoutManager.scrollToPosition(fakePosition);
                int unsnappedPosition = RecyclerView.NO_POSITION;
                if (snappedRealPosition >= 0) {
                    unsnappedPosition = snappedRealPosition;
                }
                snappedRealPosition = position;
                notifySnapped(unsnappedPosition, snappedRealPosition, totalRealItemCounts, fakePosition);
            }
        }
    }

    public int getCurrentSnappedPosition() {
        return snappedRealPosition;
    }

    protected void notifySnapped(int unsnappedPosition, int snappedPosition, int itemCounts, int snappedFakePosition) {
        snappedFakeViewPosition = snappedFakePosition;
        super.notifySnapped(unsnappedPosition, snappedPosition, itemCounts);
    }

    @Override
    public void snapToNext() {
        RecyclerView recyclerView = getRecyclerView();
        if (null == recyclerView) {
            return;
        }

        if (!(recyclerView.getAdapter() instanceof LoopPagerRecyclerViewAdapter)) {
            super.snapToNext();
            return;
        }

        if (isScrollable()) {
            LoopPagerRecyclerViewAdapter adapter = (LoopPagerRecyclerViewAdapter) recyclerView.getAdapter();
            if (null == adapter) {
                return;
            }

            int fakeViewPosition = snappedFakeViewPosition;
            if ((fakeViewPosition == RecyclerView.NO_POSITION) || (fakeViewPosition >= adapter.getItemCount() - 1)) {
                scrollToSnapPosition(0);
                return;
            }

            int unsnappedPosition = adapter.getRealPosition(fakeViewPosition);
            int snappedPosition = adapter.getRealPosition(fakeViewPosition + 1);

            recyclerView.smoothScrollToPosition(fakeViewPosition + 1);
            notifySnapped(unsnappedPosition, snappedPosition, adapter.getRealItemCount(), fakeViewPosition + 1);

            this.snappedRealPosition = snappedPosition;
            this.snappedFakeViewPosition = fakeViewPosition + 1;
        }
    }

    @Override
    public void snapToPrevious() {
        RecyclerView recyclerView = getRecyclerView();
        if (null == recyclerView) {
            return;
        }

        if (!(recyclerView.getAdapter() instanceof LoopPagerRecyclerViewAdapter)) {
            super.snapToPrevious();
            return;
        }

        if (isScrollable()) {
            LoopPagerRecyclerViewAdapter adapter = (LoopPagerRecyclerViewAdapter) recyclerView.getAdapter();
            if (null == adapter) {
                return;
            }

            int fakeViewPosition = snappedFakeViewPosition;
            if ((fakeViewPosition == RecyclerView.NO_POSITION) || (fakeViewPosition <= 0)) {
                scrollToSnapPosition(adapter.getRealItemCount() - 1);
                return;
            }

            int unsnappedPosition = adapter.getRealPosition(fakeViewPosition);
            int snappedPosition = adapter.getRealPosition(fakeViewPosition - 1);

            recyclerView.smoothScrollToPosition(fakeViewPosition - 1);
            notifySnapped(unsnappedPosition, snappedPosition, adapter.getRealItemCount(), fakeViewPosition - 1);

            this.snappedRealPosition = snappedPosition;
            this.snappedFakeViewPosition = fakeViewPosition - 1;
        }
    }
}
