package com.herry.loopsnaprecyclerview.lib;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

@SuppressWarnings("unused")
public class AutoLoopPagerSnapExHelper extends LoopPagerSnapExHelper {
    // auto scrolling loop pager snap
    private long period = 0;

    private final Object timerLock = new Object();
    private static final int MSG_NEXT = 1;
    @SuppressLint("HandlerLeak")
    private Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEXT : {
                    RecyclerView recyclerView = getRecyclerView();
                    if ((null != recyclerView)
                            && recyclerView.isAttachedToWindow()
                            && recyclerView.isShown()
                            && (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)) {
                        snapToNext();
                    }
                    sendMessageNext(period);
                    break;
                }
            }
        }
    };

    private void sendMessageNext(long period) {
        synchronized (timerLock) {
            timerHandler.removeCallbacksAndMessages(null);
            if (0 < period) {
                Message message = new Message();
                message.what = MSG_NEXT;
                timerHandler.sendMessageDelayed(message, period);
            }
        }
    }

    private void enableAuto(boolean enable) {
        RecyclerView recyclerView = getRecyclerView();
        if (null == recyclerView) {
            return;
        }

        stopTimer();

        if (enable) {
            sendMessageNext(period);
        }
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void startAuto(long period) {
        enableAuto(false);

        this.period = period;
        if (0 < this.period) {
            enableAuto(true);
        }
    }

    public void startAuto() {
        if (0 < this.period) {
            enableAuto(true);
        }
    }

    public void stopAuto() {
        enableAuto(false);
    }

    private void stopTimer() {
        synchronized (timerLock) {
            timerHandler.removeCallbacksAndMessages(null);
        }
    }
}