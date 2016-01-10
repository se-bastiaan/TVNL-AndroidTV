package eu.se_bastiaan.tvnl.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RadioboxBroadcast {

    private Long id;
    @SerializedName("startdatetime")
    private Date startTime;
    @SerializedName("stopdatetime")
    private Date stopTime;
    private String name;
    private String subname;
    private String description;
    @SerializedName("PRID")
    private String prId;
    private RadioboxImage image;

    public Long getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public String getDescription() {
        return description;
    }

    public String getPrId() {
        return prId;
    }

    public RadioboxImage getImage() {
        return image;
    }

}
