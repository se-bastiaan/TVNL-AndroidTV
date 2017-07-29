package eu.se_bastiaan.tvnl.api.model.page.component.filter

data class Option(val display : String, val default : Boolean = false,
                  val selected : Boolean = false, val value : String)