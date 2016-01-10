package eu.se_bastiaan.tvnl.model;

import java.util.List;

public class RadioboxChannel {

    private Long id;
    private String name;
    private List<RadioboxVideostream> videostream;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<RadioboxVideostream> getVideostream() {
        return videostream;
    }
}
