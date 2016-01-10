package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Episode implements Parcelable {

    @SerializedName("whatson_id")
    private String whatsOnId;
    private String mid;
    @SerializedName("nebo_id")
    private String neboId;
    private String name;
    private String description;
    private List<String> broadcasters;
    private List<String> genres;
    private List<Still> stills;
    private String image;
    private Long duration;
    private Boolean revoked;
    private List<String> video;
    private Integer views;
    private List<String> advisories;
    private Boolean active;
    @SerializedName("broadcasted_at")
    private Long broadcastedAt;
    private Restrictions restrictions;
    private Series series;

    public String getWhatsOnId() {
        return whatsOnId;
    }

    public String getId() {
        return mid;
    }

    public String getNeboId() {
        return neboId;
    }

    public String getName() {
        if(name == null || name.isEmpty()) {
            return getSeries().getName();
        }
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getBroadcasters() {
        return broadcasters;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<Still> getStills() {
        return stills;
    }

    public String getImage() {
        if(image == null && stills != null && stills.size() > 0) {
            return stills.get(0).getUrl();
        }
        return image;
    }

    public Long getDuration() {
        return duration;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public List<String> getVideo() {
        return video;
    }

    public Integer getViews() {
        return views;
    }

    public List<String> getAdvisories() {
        return advisories;
    }

    public Boolean getActive() {
        return active;
    }

    public Date getBroadcastedAt() {
        return new Date(broadcastedAt * 1000);
    }

    public String getBroadcastedAtAsStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E d MMM • H:mm", Locale.getDefault());
        return simpleDateFormat.format(getBroadcastedAt());
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public OverviewGridItem<Episode> toOverviewGridItem() {
        return new OverviewGridItem<>(this, getName(), TextUtils.join(", ", getBroadcasters()), getImage());
    }


    protected Episode(Parcel in) {
        whatsOnId = in.readString();
        mid = in.readString();
        neboId = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0x01) {
            broadcasters = new ArrayList<String>();
            in.readList(broadcasters, String.class.getClassLoader());
        } else {
            broadcasters = null;
        }
        if (in.readByte() == 0x01) {
            genres = new ArrayList<String>();
            in.readList(genres, String.class.getClassLoader());
        } else {
            genres = null;
        }
        if (in.readByte() == 0x01) {
            stills = new ArrayList<Still>();
            in.readList(stills, Still.class.getClassLoader());
        } else {
            stills = null;
        }
        image = in.readString();
        duration = in.readByte() == 0x00 ? null : in.readLong();
        byte revokedVal = in.readByte();
        revoked = revokedVal == 0x02 ? null : revokedVal != 0x00;
        if (in.readByte() == 0x01) {
            video = new ArrayList<String>();
            in.readList(video, String.class.getClassLoader());
        } else {
            video = null;
        }
        views = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            advisories = new ArrayList<String>();
            in.readList(advisories, String.class.getClassLoader());
        } else {
            advisories = null;
        }
        byte activeVal = in.readByte();
        active = activeVal == 0x02 ? null : activeVal != 0x00;
        broadcastedAt = in.readByte() == 0x00 ? null : in.readLong();
        restrictions = (Restrictions) in.readValue(Restrictions.class.getClassLoader());
        series = (Series) in.readValue(Series.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(whatsOnId);
        dest.writeString(mid);
        dest.writeString(neboId);
        dest.writeString(name);
        dest.writeString(description);
        if (broadcasters == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(broadcasters);
        }
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        if (stills == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(stills);
        }
        dest.writeString(image);
        if (duration == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(duration);
        }
        if (revoked == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (revoked ? 0x01 : 0x00));
        }
        if (video == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(video);
        }
        if (views == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(views);
        }
        if (advisories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(advisories);
        }
        if (active == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (active ? 0x01 : 0x00));
        }
        if (broadcastedAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(broadcastedAt);
        }
        dest.writeValue(restrictions);
        dest.writeValue(series);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };
}
