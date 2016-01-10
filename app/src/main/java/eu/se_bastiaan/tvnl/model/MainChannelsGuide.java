package eu.se_bastiaan.tvnl.model;

import java.util.List;

public class MainChannelsGuide {

    private String date;
    private List<Broadcast> ned1;
    private List<Broadcast> ned2;
    private List<Broadcast> ned3;

    public String getDate() {
        return date;
    }

    public List<Broadcast> getNPO1() {
        return ned1;
    }

    public List<Broadcast> getNPO2() {
        return ned2;
    }

    public List<Broadcast> getNPO3() {
        return ned3;
    }

}
