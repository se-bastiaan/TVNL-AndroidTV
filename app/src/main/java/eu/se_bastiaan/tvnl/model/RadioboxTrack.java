package eu.se_bastiaan.tvnl.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RadioboxTrack {

    private Long id;
    @SerializedName("startdatetime")
    private Date startTime;
    @SerializedName("stopdatetime")
    private Date stopTime;
    private String date;
    private RadioboxSongfile songfile;
    private Integer channel;

    public Long getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public String getDate() {
        return date;
    }

    public RadioboxSongfile getSongfile() {
        return songfile;
    }

    public Integer getChannel() {
        return channel;
    }

}
