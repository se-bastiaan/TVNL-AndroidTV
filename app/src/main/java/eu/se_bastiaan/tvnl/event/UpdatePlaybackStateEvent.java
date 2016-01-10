package eu.se_bastiaan.tvnl.event;

public class UpdatePlaybackStateEvent {

    private boolean isPlaying;

    public UpdatePlaybackStateEvent(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}