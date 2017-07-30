package eu.se_bastiaan.tvnl.api.model.page.component

import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.data.Asset
import eu.se_bastiaan.tvnl.api.model.page.component.data.Series
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter


class EpisodeHeaderComponent(data: Data, filter: Filter, isPlaceHolder: Boolean,
                             links: Map<String, Link>, mediaTarget: String,
                             target: String, targetedBy: List<String>,
                             targets: List<String>, title: String?,
                             val episode : Asset, val series : Series?,
                             id : String) : AbstractComponent(data, filter, isPlaceHolder,
        links, mediaTarget, target, targetedBy, targets, title,
        AbstractComponent.EPISODE_HEADER, id)