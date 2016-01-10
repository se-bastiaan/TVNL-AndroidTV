package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Still implements Parcelable {

    private String url;
    private Integer offset;

    public String getUrl() {
        return url;
    }

    public Integer getOffset() {
        return offset;
    }


    protected Still(Parcel in) {
        url = in.readString();
        offset = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        if (offset == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(offset);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Still> CREATOR = new Parcelable.Creator<Still>() {
        @Override
        public Still createFromParcel(Parcel in) {
            return new Still(in);
        }

        @Override
        public Still[] newArray(int size) {
            return new Still[size];
        }
    };

}
