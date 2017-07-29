package eu.se_bastiaan.tvnl.api.model.epg

import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset
import org.threeten.bp.ZonedDateTime

data class Schedule(val endsAt : ZonedDateTime, val startsAt : ZonedDateTime,
                    val highlighted : Boolean, val page : String,
                    val program : AbstractAsset)