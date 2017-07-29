package eu.se_bastiaan.tvnl.api.model.entity

data class Tab(val components : List<String>, val default : Boolean,
               val id : String, val title : String)