package eu.se_bastiaan.tvnl.event;

public class SeekBackwardEvent {
    public static final int MINIMUM_SEEK_SPEED = 2000;
    private int seek = MINIMUM_SEEK_SPEED;

    public SeekBackwardEvent() {
        setSeek(MINIMUM_SEEK_SPEED);
    }

    public void setSeek(int seek) {
        if (seek < 0) throw new IllegalArgumentException("Seek speed must be larger than 0");
        this.seek = seek;
    }

    public int getSeek() {
        return seek > 0 ? -1 * seek : seek;
    }
}