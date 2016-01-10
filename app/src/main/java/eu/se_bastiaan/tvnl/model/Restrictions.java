package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Restrictions implements Parcelable {

    @SerializedName("age_restriction")
    private String ageRestriction;
    @SerializedName("geoIP_restriction")
    private String geoRestriction;
    @SerializedName("time_restriction")
    private TimeRestriction timeRestriction;

    public static class TimeRestriction implements Parcelable {
        @SerializedName("online_at")
        private String onlineAt;
        @SerializedName("offline_at")
        private String offlineAt;

        protected TimeRestriction(Parcel in) {
            onlineAt = in.readString();
            offlineAt = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(onlineAt);
            dest.writeString(offlineAt);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<TimeRestriction> CREATOR = new Parcelable.Creator<TimeRestriction>() {
            @Override
            public TimeRestriction createFromParcel(Parcel in) {
                return new TimeRestriction(in);
            }

            @Override
            public TimeRestriction[] newArray(int size) {
                return new TimeRestriction[size];
            }
        };
    }


    protected Restrictions(Parcel in) {
        ageRestriction = in.readString();
        geoRestriction = in.readString();
        timeRestriction = (TimeRestriction) in.readValue(TimeRestriction.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ageRestriction);
        dest.writeString(geoRestriction);
        dest.writeValue(timeRestriction);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Restrictions> CREATOR = new Parcelable.Creator<Restrictions>() {
        @Override
        public Restrictions createFromParcel(Parcel in) {
            return new Restrictions(in);
        }

        @Override
        public Restrictions[] newArray(int size) {
            return new Restrictions[size];
        }
    };
}