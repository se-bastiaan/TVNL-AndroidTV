package eu.se_bastiaan.tvnl.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v17.leanback.app.BackgroundManager;
import android.view.Display;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.TimeUnit;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class BackgroundUpdater {

    private final static int BACKGROUND_UPDATE_DELAY = 300;
    private int defaultBackground;
    private Context context;
    private Target backgroundImageTarget;
    private Subscription backgroundSubscription;
    private BackgroundManager backgroundManager;
    private String backgroundUrl;

    public static BackgroundUpdater init(Activity activity, @DrawableRes int defaultBackground) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        BackgroundUpdater updater = new BackgroundUpdater();
        updater.context = activity.getApplicationContext();

        updater.backgroundManager = BackgroundManager.getInstance(activity);
        updater.backgroundManager.attach(activity.getWindow());
        updater.backgroundImageTarget = new GlideBackgroundManagerTarget(size.x, size.y, updater.backgroundManager);
        updater.defaultBackground = defaultBackground;

        return updater;
    }

    private BackgroundUpdater() {
    }


    /**
     * Updates the background asynchronously with the given image url, after a short delay.
     *
     * @param url String
     */
    public void updateBackgroundAsync(String url) {
        if(backgroundUrl != null && backgroundUrl.equals(url))
            return;

        backgroundUrl = url;
        backgroundSubscription = Single.just(backgroundUrl)
                .delay(BACKGROUND_UPDATE_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Action1<String>() {
                    @Override
                    public void call(String backgroundUrl) {
                        if (backgroundUrl != null) {
                            updateBackground(backgroundUrl);
                        }
                    }
                })
                .subscribe();
    }


    private void updateBackground(String url) {
        clearBackground();

        if (null != backgroundSubscription && !backgroundSubscription.isUnsubscribed()) {
            backgroundSubscription.unsubscribe();
        }

        if (null == url)
            return;

        //load actual background image
        Glide.with(context)
                .load(url)
                .centerCrop()
                .bitmapTransform(new BlurTransformation(context, 10))
                .error(defaultBackground)
                .into(backgroundImageTarget);
    }

    protected void setDefaultBackground(int resourceId) {
        defaultBackground = resourceId;
    }

    /**
     * Updates the background immediately with a drawable
     *
     * @param drawable Drawable
     */
    public void updateBackground(Drawable drawable) {
        backgroundManager.setDrawable(drawable);
    }

    /**
     * Updates the background immediately with a drawable
     *
     * @param drawable Drawable
     */
    public void updateBackground(@DrawableRes int drawable) {
        Glide.with(context)
                .load(drawable)
                .into(backgroundImageTarget);
    }

    /**
     * Clears the background immediately
     */
    public void clearBackground() {
        Glide.with(context)
                .load(defaultBackground)
                .into(backgroundImageTarget);
    }

    public void destroy() {
        if (null != backgroundSubscription && !backgroundSubscription.isUnsubscribed()) {
            backgroundSubscription.unsubscribe();
        }

        Glide.clear(backgroundImageTarget);
        backgroundManager.release();
    }
}