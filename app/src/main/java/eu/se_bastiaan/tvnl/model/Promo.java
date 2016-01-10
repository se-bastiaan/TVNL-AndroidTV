package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Promo implements Parcelable {

    private String mid;
    private String name;
    @SerializedName("episode_mid")
    private String episodeMid;
    @SerializedName("online_at")
    private Long onlineAt;
    @SerializedName("offline_at")
    private Long offlineAt;
    private List<VideoLocation> locations;
    private Episode episode;

    public String getId() {
        return mid;
    }

    public String getName() {
        return name;
    }

    public String getEpisodeId() {
        return episodeMid;
    }

    public Long getOnlineAt() {
        return onlineAt;
    }

    public Long getOfflineAt() {
        return offlineAt;
    }

    public List<VideoLocation> getLocations() {
        return locations;
    }

    public Episode getEpisode() {
        return episode;
    }

    public OverviewGridItem<Promo> toOverviewGridItem() {
        return new OverviewGridItem<>(this, name, episode.getBroadcasters().toString(), episode.getImage());
    }


    protected Promo(Parcel in) {
        mid = in.readString();
        name = in.readString();
        episodeMid = in.readString();
        onlineAt = in.readByte() == 0x00 ? null : in.readLong();
        offlineAt = in.readByte() == 0x00 ? null : in.readLong();
        if (in.readByte() == 0x01) {
            locations = new ArrayList<>();
            in.readList(locations, VideoLocation.class.getClassLoader());
        } else {
            locations = null;
        }
        episode = (Episode) in.readValue(Episode.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mid);
        dest.writeString(name);
        dest.writeString(episodeMid);
        if (onlineAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(onlineAt);
        }
        if (offlineAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(offlineAt);
        }
        if (locations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(locations);
        }
        dest.writeValue(episode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Promo> CREATOR = new Parcelable.Creator<Promo>() {
        @Override
        public Promo createFromParcel(Parcel in) {
            return new Promo(in);
        }

        @Override
        public Promo[] newArray(int size) {
            return new Promo[size];
        }
    };
}