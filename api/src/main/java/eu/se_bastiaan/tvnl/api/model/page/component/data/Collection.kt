package eu.se_bastiaan.tvnl.api.model.page.component.data

import eu.se_bastiaan.tvnl.api.model.entity.Image
import eu.se_bastiaan.tvnl.api.model.entity.Link

class Collection(broadcasters: List<String>, description: String,
                 id: String, images: Map<String, Image>,
                 isOnlyOnNpoPlus: Boolean, links: Map<String, Link>,
                 onDemand: Boolean, title: String) :
        AbstractAsset(broadcasters, description, id,
                images, isOnlyOnNpoPlus, links, onDemand, title, AbstractAsset.COLLECTION)