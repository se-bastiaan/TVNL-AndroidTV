package eu.se_bastiaan.tvnl.network;

import android.os.SystemClock;

import java.io.IOException;

import rx.Single;
import rx.SingleSubscriber;

public class RxSntpClient {

    public static Single<Long> getCurrentTime(final String host, final int timeout) {
        return Single.create(new Single.OnSubscribe<Long>() {
            @Override
            public void call(SingleSubscriber<? super Long> subscriber) {
                try {
                    SntpClient client = new SntpClient();
                    if (client.requestTime(host, timeout)) {
                        long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
                        subscriber.onSuccess(now);
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

}
