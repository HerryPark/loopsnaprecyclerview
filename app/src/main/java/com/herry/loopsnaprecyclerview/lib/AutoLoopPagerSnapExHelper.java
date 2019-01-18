package com.herry.loopsnaprecyclerview.lib;

import android.support.v7.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

public class AutoLoopPagerSnapExHelper extends LoopPagerSnapExHelper {
    // auto scrolling loop pager snap
    private Timer timer = null;
    private long period = 0;

    public void startAuto(long period) {
        enableAuto(false);

        this.period = period;
        if (0 < this.period) {
            enableAuto(true);
        }
    }

    public void stopAuto() {
        enableAuto(false);

        this.period = 0;
    }

    private void enableAuto(boolean enable) {
        RecyclerView recyclerView = getRecyclerView();
        if (null == recyclerView) {
            return;
        }

        if (enable) {
            if (period > 0) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = getRecyclerView();
                        if (null == recyclerView || !recyclerView.isAttachedToWindow()) {
                            return;
                        }

                        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    snapToNext();
                                }
                            });
                        }
                    }
                }, period, period);
            }
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }
}
