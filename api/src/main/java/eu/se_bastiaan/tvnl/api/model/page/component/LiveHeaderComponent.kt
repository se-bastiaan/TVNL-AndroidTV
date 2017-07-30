package eu.se_bastiaan.tvnl.api.model.page.component

import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.epg.Epg
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter


class LiveHeaderComponent(data: Data, filter: Filter, isPlaceHolder: Boolean,
                          links: Map<String, Link>, mediaTarget: String,
                          target: String, targetedBy: List<String>, targets: List<String>,
                          title: String?, id: String, val epg: Epg) :
        AbstractComponent(data, filter, isPlaceHolder, links, mediaTarget,
                target, targetedBy, targets, title, AbstractComponent.LIVE_HEADER, id)