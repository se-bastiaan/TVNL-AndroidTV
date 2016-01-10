package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoLocation implements Parcelable {

    private String format;
    private String url;

    public String getFormat() {
        return format;
    }

    public String getUrl() {
        return url;
    }


    protected VideoLocation(Parcel in) {
        format = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(format);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideoLocation> CREATOR = new Parcelable.Creator<VideoLocation>() {
        @Override
        public VideoLocation createFromParcel(Parcel in) {
            return new VideoLocation(in);
        }

        @Override
        public VideoLocation[] newArray(int size) {
            return new VideoLocation[size];
        }
    };
}