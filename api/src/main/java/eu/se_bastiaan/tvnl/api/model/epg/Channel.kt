package eu.se_bastiaan.tvnl.api.model.epg

import eu.se_bastiaan.tvnl.api.model.entity.Image
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset

class Channel(broadcasters: List<String>, description: String,
              id: String, images: Map<String, Image>,
              isOnlyOnNpoPlus: Boolean, links: Map<String, Link>,
              onDemand: Boolean, title: String, type: String,
              val channel: String, val externalRadioUrl: String?,
              val liveStream: AbstractAsset?, val name: String) :
        AbstractAsset(broadcasters, description,
                id, images,
                isOnlyOnNpoPlus, links,
                onDemand, title, type)