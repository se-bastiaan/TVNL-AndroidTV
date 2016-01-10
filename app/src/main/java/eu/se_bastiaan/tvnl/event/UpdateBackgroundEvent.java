package eu.se_bastiaan.tvnl.event;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

public class UpdateBackgroundEvent {

    private Drawable drawable;
    private String url;
    @DrawableRes
    private Integer res = -1;

    public UpdateBackgroundEvent() {
        drawable = null;
        url = null;
    }

    public UpdateBackgroundEvent(Drawable drawable) {
        this.drawable = drawable;
    }

    public UpdateBackgroundEvent(String url) {
        this.url = url;
    }

    public UpdateBackgroundEvent(@DrawableRes Integer res) {
        this.res = res;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getUrl() {
        return url;
    }

    public Integer getResource() {
        return res;
    }

    public Boolean isDrawable() {
        return drawable != null;
    }

    public Boolean isUrl() {
        return url != null;
    }

    public Boolean isResource() {
        return res != -1;
    }

}
