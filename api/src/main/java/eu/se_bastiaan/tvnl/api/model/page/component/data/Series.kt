/*
 * Copyright (C) 2017 Sébastiaan (github.com/se-bastiaan)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see . Also add information on how to contact you by electronic and paper mail.
 */

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