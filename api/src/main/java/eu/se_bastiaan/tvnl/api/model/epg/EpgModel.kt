package eu.se_bastiaan.tvnl.api.model.epg

import java.util.*

data class EpgModel(val date : String, val epg : List<Epg> = ArrayList())