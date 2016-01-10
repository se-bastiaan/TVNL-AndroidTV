package eu.se_bastiaan.tvnl.util;

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.app.BackgroundManager;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class GlideBackgroundManagerTarget extends SimpleTarget<GlideDrawable> {

    BackgroundManager backgroundManager;

    public GlideBackgroundManagerTarget(int width, int height, BackgroundManager backgroundManager) {
        super(width, height);
        this.backgroundManager = backgroundManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GlideBackgroundManagerTarget that = (GlideBackgroundManagerTarget) o;

        return backgroundManager.equals(that.backgroundManager);
    }

    @Override
    public int hashCode() {
        return backgroundManager.hashCode();
    }

    @Override
    public void onResourceReady(GlideDrawable drawable, GlideAnimation glideAnimation) {
        backgroundManager.setDrawable(null);
        backgroundManager.setDrawable(drawable);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        backgroundManager.setDrawable(errorDrawable);
    }

}