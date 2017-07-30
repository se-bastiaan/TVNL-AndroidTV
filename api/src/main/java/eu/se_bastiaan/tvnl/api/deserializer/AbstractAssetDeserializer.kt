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

package eu.se_bastiaan.tvnl.api.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import eu.se_bastiaan.tvnl.api.model.epg.Channel
import eu.se_bastiaan.tvnl.api.model.page.component.data.*
import eu.se_bastiaan.tvnl.api.model.page.component.data.Collection
import java.lang.reflect.Type

class AbstractAssetDeserializer : JsonDeserializer<AbstractAsset> {
    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): AbstractAsset? {
        if (element is JsonObject && element.has("type")) {
            val str = element.get("type").asString
            when (str) {
                AbstractAsset.SEASON -> return context.deserialize<AbstractAsset>(element, Season::class.java)
                AbstractAsset.SERIES -> return context.deserialize<AbstractAsset>(element, Series::class.java)
                AbstractAsset.COLLECTION -> return context.deserialize<AbstractAsset>(element, Collection::class.java)
                AbstractAsset.RADIO_CHANNEL -> return context.deserialize<AbstractAsset>(element, Channel::class.java)
                else -> return context.deserialize<AbstractAsset>(element, Asset::class.java)
            }
        }
        return null
    }
}