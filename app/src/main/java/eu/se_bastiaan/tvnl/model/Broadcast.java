package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Broadcast implements Parcelable {

    private String id;
    private Boolean rerun;
    @SerializedName("starts_at")
    private Long startsAt;
    @SerializedName("ends_at")
    private Long endsAt;
    private Long duration;
    private Episode episode;

    public String getId() {
        return id;
    }

    public Boolean getRerun() {
        return rerun;
    }

    public Date getStartsAt() {
        return new Date(startsAt * 1000);
    }

    public Date getEndsAt() {
        return new Date(endsAt * 1000);
    }

    public Long getDuration() {
        return duration;
    }

    public Episode getEpisode() {
        return episode;
    }

    public OverviewGridItem<Broadcast> toOverviewGridItem() {
        return new OverviewGridItem<>(this, episode.getName(), episode.getBroadcasters().toString(), episode.getImage());
    }

    protected Broadcast(Parcel in) {
        id = in.readString();
        byte rerunVal = in.readByte();
        rerun = rerunVal == 0x02 ? null : rerunVal != 0x00;
        startsAt = in.readByte() == 0x00 ? null : in.readLong();
        endsAt = in.readByte() == 0x00 ? null : in.readLong();
        duration = in.readByte() == 0x00 ? null : in.readLong();
        episode = (Episode) in.readValue(Episode.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (rerun == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (rerun ? 0x01 : 0x00));
        }
        if (startsAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(startsAt);
        }
        if (endsAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(endsAt);
        }
        if (duration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(duration);
        }
        dest.writeValue(episode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Broadcast> CREATOR = new Parcelable.Creator<Broadcast>() {
        @Override
        public Broadcast createFromParcel(Parcel in) {
            return new Broadcast(in);
        }

        @Override
        public Broadcast[] newArray(int size) {
            return new Broadcast[size];
        }
    };
}