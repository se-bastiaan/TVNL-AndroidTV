package eu.se_bastiaan.tvnl.util;

import java.util.Locale;

public class DurationUtil {

    public static String convert(Long seconds) {
        if(seconds < 57) {
            return String.format(Locale.getDefault(), "%d sec", seconds);
        }
        int minutes = (int) Math.ceil(Math.ceil(seconds) / 60);
        return String.format(Locale.getDefault(), "%d min", minutes);
    }

}
