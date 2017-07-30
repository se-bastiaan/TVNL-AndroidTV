package eu.se_bastiaan.tvnl.api.model.page.component

import com.google.gson.annotations.SerializedName
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter
import java.util.*


class FilterComponent(data: Data, filter: Filter, isPlaceHolder: Boolean,
                      links: Map<String, Link>, mediaTarget: String,
                      target: String, targetedBy: List<String>,
                      targets: List<String>, title: String?, id: String,
                      @SerializedName("filters") val filters: List<Filter> = ArrayList()) :
        AbstractComponent(data, filter, isPlaceHolder, links, mediaTarget, target,
                targetedBy, targets, title, AbstractComponent.FILTER, id)