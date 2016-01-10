package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StreamInfo implements Parcelable {

    private String id;
    private String videoLocation;
    private String title;
    private String subtitle;
    private String image;
    private Boolean isLive;
    private Long startPosition = 0L;

    public StreamInfo(String id, String videoLocation, String title, String subtitle, String image) {
        this(id, videoLocation, title, subtitle, image, false);
    }

    public StreamInfo(String id, String videoLocation, String title, String subtitle, String image, Long startPosition) {
        this(id, videoLocation, title, subtitle, image, false);
        this.startPosition = startPosition;
    }

    public StreamInfo(String id, String videoLocation, String title, String subtitle, String image, Boolean isLive) {
        this.id = id;
        this.videoLocation = videoLocation;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.isLive = isLive;
    }

    public String getId() {
        return id;
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImage() {
        return image;
    }

    public Boolean isLive() {
        return isLive;
    }

    public Long getStartPosition() {
        return startPosition;
    }

    protected StreamInfo(Parcel in) {
        id = in.readString();
        videoLocation = in.readString();
        title = in.readString();
        subtitle = in.readString();
        image = in.readString();
        isLive = in.readInt() == 1;
        startPosition = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(videoLocation);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(image);
        dest.writeInt(isLive ? 1 : 0);
        dest.writeLong(startPosition);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StreamInfo> CREATOR = new Parcelable.Creator<StreamInfo>() {
        @Override
        public StreamInfo createFromParcel(Parcel in) {
            return new StreamInfo(in);
        }

        @Override
        public StreamInfo[] newArray(int size) {
            return new StreamInfo[size];
        }
    };
}