package eu.se_bastiaan.tvnl.api.model.entity

import org.threeten.bp.ZonedDateTime

data class Availability(val from : ZonedDateTime,
                        val prediction : ZonedDateTime? = null,
                        val to : ZonedDateTime)