/*
 * Copyright (C) 2017 Sébastiaan (github.com/se-bastiaan)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see . Also add information on how to contact you by electronic and paper mail.
 */

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