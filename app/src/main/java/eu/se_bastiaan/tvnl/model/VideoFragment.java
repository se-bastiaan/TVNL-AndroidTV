package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import eu.se_bastiaan.tvnl.util.DurationUtil;

public class VideoFragment implements Parcelable {

    private String mid;
    private String name;
    private String description;
    @SerializedName("starts_at")
    private Long startsAt;
    @SerializedName("ends_at")
    private Long endsAt;
    private Long duration;
    private Episode episode;
    private List<Still> stills;

    public String getId() {
        return mid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getStartsAt() {
        return startsAt;
    }

    public Long getEndsAt() {
        return endsAt;
    }

    public Long getDuration() {
        return duration;
    }

    public Episode getEpisode() {
        return episode;
    }

    public List<Still> getStills() {
        return stills;
    }

    public OverviewGridItem<VideoFragment> toOverviewGridItem() {
        String subtitle = String.format(Locale.getDefault(), "%s • Uit: %s", DurationUtil.convert(duration), episode.getSeries().getName());

        if(stills == null)
            return new OverviewGridItem<>(this, name, subtitle, null);
        return new OverviewGridItem<>(this, name, subtitle, stills.get(0).getUrl());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mid);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeValue(this.startsAt);
        dest.writeValue(this.endsAt);
        dest.writeValue(this.duration);
        dest.writeParcelable(this.episode, 0);
        dest.writeTypedList(stills);
    }

    public VideoFragment() {
    }

    protected VideoFragment(Parcel in) {
        this.mid = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.startsAt = (Long) in.readValue(Long.class.getClassLoader());
        this.endsAt = (Long) in.readValue(Long.class.getClassLoader());
        this.duration = (Long) in.readValue(Long.class.getClassLoader());
        this.episode = in.readParcelable(Episode.class.getClassLoader());
        this.stills = in.createTypedArrayList(Still.CREATOR);
    }

    public static final Parcelable.Creator<VideoFragment> CREATOR = new Parcelable.Creator<VideoFragment>() {
        public VideoFragment createFromParcel(Parcel source) {
            return new VideoFragment(source);
        }

        public VideoFragment[] newArray(int size) {
            return new VideoFragment[size];
        }
    };

}