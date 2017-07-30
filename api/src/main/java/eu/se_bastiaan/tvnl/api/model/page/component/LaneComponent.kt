package eu.se_bastiaan.tvnl.api.model.page.component

import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter
import eu.se_bastiaan.tvnl.api.model.page.component.tile.TileType


class LaneComponent(data: Data, filter: Filter, isPlaceHolder: Boolean,
                    links: Map<String, Link>, mediaTarget: String,
                    target: String, targetedBy: List<String>,
                    targets: List<String>, title: String?, id: String,
                    @TileType val tileMapping: String, @TileType val tileType: String) :
        AbstractComponent(data, filter, isPlaceHolder, links, mediaTarget, target,
                targetedBy, targets, title, AbstractComponent.LANE, id)
