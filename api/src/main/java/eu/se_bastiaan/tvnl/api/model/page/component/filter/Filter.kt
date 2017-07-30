package eu.se_bastiaan.tvnl.api.model.page.component.filter

data class Filter(val filterArgument : String = "", val filterType : String = "",
                  val options : List<Option> = ArrayList(), val title : String = "")