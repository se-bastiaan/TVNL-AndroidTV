package eu.se_bastiaan.tvnl.api.model.entity

data class Subtitle(val default : Boolean = false, val label : String,
                    val language : String, val src : String)