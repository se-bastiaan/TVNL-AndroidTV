package eu.se_bastiaan.tvnl.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RadioboxAudiofragment {

    private Long id;
    private String url;
    private String description;
    @SerializedName("startdatetime")
    private Date startDate;
    @SerializedName("stopdatetime")
    private Date stopDate;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

}
