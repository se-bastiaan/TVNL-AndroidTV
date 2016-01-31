package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Series implements Parcelable {

    private String mid;
    private String name;
    private String image;
    @SerializedName("nebo_id")
    private String neboId;
    private List<String> genres;
    private List<String> broadcasters;
    private Long episodeCount;
    private List<Episode> episodes;
    private Long code;
    @SerializedName("starts_with")
    private String startsWith;

    public String getId() {
        return mid;
    }

    public String getName() {
        return name;
    }

    public String getNeboId() {
        return neboId;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Long getEpisodeCount() {
        return episodeCount;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public Long getCode() {
        return code;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public List<String> getBroadcasters() {
        return broadcasters;
    }

    public String getImage() {
        return image;
    }

    public OverviewGridItem<Series> toOverviewGridItem() {
        return new OverviewGridItem<>(this, name, TextUtils.join(", ", broadcasters), getImage());
    }
    
    public Series getWithoutEpisodes() {
        return new Series(this);
    }

    private Series(Series series) {
        this.mid = series.mid;
        this.name = series.name;
        this.image = series.image;
        this.neboId = series.neboId;
        this.genres = series.genres;
        this.broadcasters = series.broadcasters;
        this.episodeCount = series.episodeCount;
        this.code = series.code;
        this.startsWith = series.startsWith;
    }

    protected Series(Parcel in) {
        mid = in.readString();
        name = in.readString();
        image = in.readString();
        neboId = in.readString();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<>();
            in.readList(genres, String.class.getClassLoader());
        } else {
            genres = null;
        }
        if (in.readByte() == 0x01) {
            broadcasters = new ArrayList<>();
            in.readList(broadcasters, String.class.getClassLoader());
        } else {
            broadcasters = null;
        }
        episodeCount = in.readByte() == 0x00 ? null : in.readLong();
        if (in.readByte() == 0x01) {
            episodes = new ArrayList<>();
            in.readList(episodes, Episode.class.getClassLoader());
        } else {
            episodes = null;
        }
        code = in.readByte() == 0x00 ? null : in.readLong();
        startsWith = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mid);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(neboId);
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        if (broadcasters == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(broadcasters);
        }
        if (episodeCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(episodeCount);
        }
        if (episodes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(episodes);
        }
        if (code == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(code);
        }
        dest.writeString(startsWith);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Series> CREATOR = new Parcelable.Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };
}
