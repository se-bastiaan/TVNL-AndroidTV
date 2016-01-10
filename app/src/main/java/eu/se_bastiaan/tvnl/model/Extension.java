package eu.se_bastiaan.tvnl.model;

public class Extension {
    public static final String SMOOTH_STREAMING = "Manifest";
    public static final String HLS = "m3u8";
    public static final String HDS = "f4m";
    // DASH appears to exist, since the Android application uses it for the livestreams, but the API (UGApiService.getLiveDataByChannel) says it doesn't. We don't have to care, since we can use Smooth Streaming, thus it's here for documentation purposes.
    public static final String DASH = "mpd";
}