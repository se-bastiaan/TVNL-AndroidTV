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