package eu.se_bastiaan.tvnl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OverviewGridItem<T> implements Parcelable {

    private T object;
    private String title;
    private String subtitle;
    private String image;

    public OverviewGridItem(T object, String title, String subtitle, String image) {
        this.object = object;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
    }

    public OverviewGridItem(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
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

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    protected OverviewGridItem(Parcel in) {
        Class type = (Class) in.readSerializable();
        object = (T) in.readValue(type.getClassLoader());
        title = in.readString();
        subtitle = in.readString();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(object.getClass());
        dest.writeValue(object);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OverviewGridItem> CREATOR = new Parcelable.Creator<OverviewGridItem>() {
        @Override
        public OverviewGridItem createFromParcel(Parcel in) {
            return new OverviewGridItem(in);
        }

        @Override
        public OverviewGridItem[] newArray(int size) {
            return new OverviewGridItem[size];
        }
    };
}