package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Timeline implements Parcelable {

    private String mid;
    private String image;
    private String kind;
    private String label;
    private String subtitle;
    private String title;
    @SerializedName("title_suffix")
    private String titleSuffix;
    private String description;
    @SerializedName("unpublish_at")
    private Long unpublishAt;

    public String getId() {
        return mid;
    }

    public String getImage() {
        return image;
    }

    public String getLabel() {
        if(kind.equals("segment")) {
            return "kort";
        } else if(kind.equals("program") && label.equals("most_watched")) {
            return "populair";
        } else if(kind.equals("program") && label.equals("tip")) {
            return "kijktip"; //
        } else if(kind.equals("promo")) {
            return "preview"; // -> play directly
        } else if(kind.equals("tv") && label.equals("tip")) {
            return "kijk live"; // -> play directly
        } else if(kind.equals("radio") && label.equals("tip")) {
            return "luister live"; // -> play directly
        }
        return "kijktip";
    }

    public boolean isLive() {
        return kind.equals("tv") || kind.equals("radio");
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleSuffix() {
        return titleSuffix;
    }

    public String getDescription() {
        return description;
    }

    public String getKind() {
        return kind;
    }

    public Long getUnpublishAt() {
        return unpublishAt;
    }

    public OverviewGridItem<Timeline> toOverviewGridItem() {
        return new OverviewGridItem<>(this, title, getLabel(), image);
    }


    protected Timeline(Parcel in) {
        mid = in.readString();
        image = in.readString();
        kind = in.readString();
        label = in.readString();
        subtitle = in.readString();
        title = in.readString();
        titleSuffix = in.readString();
        description = in.readString();
        unpublishAt = in.readByte() == 0x00 ? null : in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mid);
        dest.writeString(image);
        dest.writeString(kind);
        dest.writeString(label);
        dest.writeString(subtitle);
        dest.writeString(title);
        dest.writeString(titleSuffix);
        dest.writeString(description);
        if (unpublishAt == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(unpublishAt);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Timeline> CREATOR = new Parcelable.Creator<Timeline>() {
        @Override
        public Timeline createFromParcel(Parcel in) {
            return new Timeline(in);
        }

        @Override
        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };
}