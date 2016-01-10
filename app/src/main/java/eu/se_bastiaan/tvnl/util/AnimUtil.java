package eu.se_bastiaan.tvnl.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import eu.se_bastiaan.tvnl.TVNLApplication;

public class AnimUtil {

    public static void fadeIn(View v) {
        if (v.getVisibility() == View.VISIBLE) return;
        Animation fadeInAnim = AnimationUtils.loadAnimation(TVNLApplication.get(), android.R.anim.fade_in);
        v.setVisibility(View.VISIBLE);
        v.startAnimation(fadeInAnim);
        v.requestLayout(); // Force redraw
    }

    public static void fadeOut(View v) {
        if (v.getVisibility() == View.INVISIBLE) return;
        Animation fadeOutAnim = AnimationUtils.loadAnimation(TVNLApplication.get(), android.R.anim.fade_out);
        v.startAnimation(fadeOutAnim);
        v.setVisibility(View.INVISIBLE);
        v.requestLayout(); // Force redraw
    }

}