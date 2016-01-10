package eu.se_bastiaan.tvnl.model;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

public class Channel {

    private String id;
    private String title;
    private String subtitle;
    private int drawable;
    private int progress;
    private String image;
    private int color;
    private Boolean isRadio;

    public Channel(String id, String title, String subtitle, String image, int progress, @DrawableRes int drawable, @ColorRes int color) {
        this(id, title, subtitle, image, progress, drawable, color, false);
    }

    public Channel(String id, String title, String subtitle, String image, int progress, @DrawableRes int drawable, @ColorRes int color, boolean isRadio) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.drawable = drawable;
        this.progress = progress;
        this.image = image;
        this.color = color;
        this.isRadio = isRadio;
    }

    public String getId() {
        return id;
    }

    @ColorRes
    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    @DrawableRes
    public int getDrawable() {
        return drawable;
    }

    public String getImage() {
        return image;
    }

    public int getProgress() {
        return progress;
    }

    public Boolean isRadio() {
        return isRadio;
    }

}
