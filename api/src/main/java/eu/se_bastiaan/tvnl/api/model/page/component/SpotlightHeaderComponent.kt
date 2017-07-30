package eu.se_bastiaan.tvnl.api.model.page.component

import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter
import eu.se_bastiaan.tvnl.api.model.page.component.spotlightheader.SpotlightHeaderSlide
import java.util.*


class SpotlightHeaderComponent(data: Data, filter: Filter, isPlaceHolder: Boolean,
                               links: Map<String, Link>, mediaTarget: String,
                               target: String, targetedBy: List<String>, targets: List<String>,
                               title: String?, id: String,
                               val slides: List<SpotlightHeaderSlide> = ArrayList()) :
        AbstractComponent(data, filter, isPlaceHolder, links, mediaTarget,
                target, targetedBy, targets, title, AbstractComponent.SPOTLIGHT_HEADER, id)