package eu.se_bastiaan.tvnl.api.model.entity

import eu.se_bastiaan.tvnl.api.model.page.component.data.AbstractAsset

class Collection(broadcasters: List<String>, description: String,
                 id: String, images: Map<String, Image>,
                 isOnlyOnNpoPlus: Boolean, links: Map<String, Link>,
                 onDemand: Boolean, title: String, type: String,
                 val shareText : String) :
        AbstractAsset(broadcasters, description,
                id, images,
                isOnlyOnNpoPlus, links,
                onDemand, title, type)
