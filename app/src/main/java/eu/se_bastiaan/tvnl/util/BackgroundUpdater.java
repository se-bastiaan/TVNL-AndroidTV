/*
 * This file is part of Butter.
 *
 * Butter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Butter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Butter. If not, see <http://www.gnu.org/licenses/>.
 */

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

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundUpdater {

    private final static int BACKGROUND_UPDATE_DELAY = 300;
    private int defaultBackground;
    private Context context;
    private Target backgroundImageTarget;
    private Timer backgroundTimer;
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
        if (null != backgroundTimer) {
            backgroundTimer.cancel();
        }
        backgroundTimer = new Timer();
        backgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }


    private void updateBackground(String url) {
        clearBackground();

        if (null != backgroundTimer) {
            backgroundTimer.cancel();
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

    private class UpdateBackgroundTask extends TimerTask {
        @Override
        public void run() {
            ThreadUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (backgroundUrl != null) {
                        updateBackground(backgroundUrl);
                    }

                }
            });
        }
    }

    public void destroy() {
        if (null != backgroundTimer) {
            backgroundTimer.cancel();
        }

        Glide.clear(backgroundImageTarget);
        backgroundManager.release();
    }
}