package eu.se_bastiaan.tvnl.api.model.epg

import android.support.annotation.StringDef
import java.util.*

data class Epg(val channel: Channel, val schedules : List<Schedule> = ArrayList()) {
    companion object {
        @StringDef(DAY_AFTER_TOMORROW, DAY_BEFORE_YESTERDAY,
                TODAY, TOMORROW, YESTERDAY)
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class Day

        const val DAY_AFTER_TOMORROW = "Overmorgen"
        const val DAY_BEFORE_YESTERDAY = "Eergisteren"
        const val TODAY = "Vandaag"
        const val TOMORROW = "Morgen"
        const val YESTERDAY = "Gisteren"

        @StringDef(AFTERNOON, EVENING, MORNING)
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class DayPart

        const val AFTERNOON = "Middag"
        const val EVENING = "Avond"
        const val MORNING = "Ochtend"
    }
}