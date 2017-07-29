package eu.se_bastiaan.tvnl.api.model.page.component.data

import eu.se_bastiaan.tvnl.api.model.entity.Image
import eu.se_bastiaan.tvnl.api.model.entity.Link

abstract class AbstractAsset(
        val broadcasters: List<String> = ArrayList(), val description: String = "",
        val id: String, val images: Map<String, Image> = HashMap(),
        val isOnlyOnNpoPlus: Boolean = false, val links: Map<String, Link> = HashMap(),
        val onDemand: Boolean = false, val title: String = "",
        val type: String = "")