package eu.se_bastiaan.tvnl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeChannelsGuide {

    private String date;
    @SerializedName("cult")
    private List<Broadcast> cult;
    @SerializedName("hilv")
    private List<Broadcast> best;
    @SerializedName("holl")
    private List<Broadcast> doc;
    @SerializedName("humo")
    private List<Broadcast> humor;
    @SerializedName("nosj")
    private List<Broadcast> news;
    @SerializedName("opvo")
    private List<Broadcast> zapp;
    @SerializedName("po24")
    private List<Broadcast> pol;
    @SerializedName("_101_")
    private List<Broadcast> _101_;

    public String getDate() {
        return date;
    }

    public List<Broadcast> getCult() {
        return cult;
    }

    public List<Broadcast> getBest() {
        return best;
    }

    public List<Broadcast> getDoc() {
        return doc;
    }

    public List<Broadcast> getHumor() {
        return humor;
    }

    public List<Broadcast> getNews() {
        return news;
    }

    public List<Broadcast> getZapp() {
        return zapp;
    }

    public List<Broadcast> getPol() {
        return pol;
    }

    public List<Broadcast> get101() {
        return _101_;
    }

}
