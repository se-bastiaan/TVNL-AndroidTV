package eu.se_bastiaan.tvnl.content;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class EventBus extends Bus {
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public EventBus() {
    }

    public void postOnMain(final Object event) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            this.mainThread.post(new Runnable() {
                public void run() {
                    EventBus.this.post(event);
                }
            });
        }

    }
}