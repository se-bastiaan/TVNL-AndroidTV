package eu.se_bastiaan.tvnl.api.model.page.component.data

import eu.se_bastiaan.tvnl.api.model.entity.Image
import eu.se_bastiaan.tvnl.api.model.entity.Link

class Series(broadcasters: List<String>, description: String,
                 id: String, images: Map<String, Image>,
                 isOnlyOnNpoPlus: Boolean, links: Map<String, Link>,
                 onDemand: Boolean, title: String,
             val facebookUrl : String? = null, val googlePlusUrl : String? = null,
             val instagramUrl : String? = null, val pinterestUrl : String? = null,
             val shareText : String? = null, val shareUrl : String? = null,
             val snapchatUrl : String? = null, val twitterUrl : String? = null,
             val websiteUrl : String? = null) :
        AbstractAsset(broadcasters, description, id,
                images, isOnlyOnNpoPlus, links, onDemand, title, AbstractAsset.SERIES)