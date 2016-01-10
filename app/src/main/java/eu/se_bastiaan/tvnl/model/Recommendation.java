package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Recommendation implements Parcelable {

    private String id;
    private String name;
    private String description;
    @SerializedName("image")
    private String image;
    @SerializedName("published_at")
    private Long publishedAt;
    private Long position;
    private Episode episode;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public Date getPublishedAt() {
        return new Date(publishedAt * 1000);
    }

    public Long getPosition() {
        return position;
    }

    public Episode getEpisode() {
        return episode;
    }

    public OverviewGridItem<Recommendation> toOverviewGridItem() {
        return new OverviewGridItem<>(this, name, episode.getBroadcasters().toString(), image);
    }


    protected Recommendation(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        publishedAt = in.readByte() == 0x00 ? null : in.readLong();
        position = in.readByte() == 0x00 ? null : in.readLong();
        episode = (Episode) in.readValue(Episode.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        if (publishedAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(publishedAt);
        }
        if (position == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(position);
        }
        dest.writeValue(episode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recommendation> CREATOR = new Parcelable.Creator<Recommendation>() {
        @Override
        public Recommendation createFromParcel(Parcel in) {
            return new Recommendation(in);
        }

        @Override
        public Recommendation[] newArray(int size) {
            return new Recommendation[size];
        }
    };
}