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
import eu.se_bastiaan.tvnl.api.model.page.component.*
import java.lang.reflect.Type

class AbstractComponentDeserializer : JsonDeserializer<AbstractComponent> {
    override fun deserialize(paramJsonElement: JsonElement, type: Type,
                             context: JsonDeserializationContext): AbstractComponent? {
        if (paramJsonElement is JsonObject && paramJsonElement.has("type")) {
            val str = paramJsonElement.get("type").asString
            when (str) {
                AbstractComponent.BANNER -> return context.deserialize<Any>(paramJsonElement,
                        BannerComponent::class.java) as AbstractComponent
                AbstractComponent.COLLECTION_HEADER -> return context.deserialize<Any>(paramJsonElement,
                        CollectionHeaderComponent::class.java) as AbstractComponent
                AbstractComponent.EPISODE_HEADER -> return context.deserialize<Any>(paramJsonElement,
                        EpisodeHeaderComponent::class.java) as AbstractComponent
                AbstractComponent.FILTER -> return context.deserialize<Any>(paramJsonElement,
                        FilterComponent::class.java) as AbstractComponent
                AbstractComponent.FIVE_GRID -> return context.deserialize<Any>(paramJsonElement,
                        FiveGridComponent::class.java) as AbstractComponent
                AbstractComponent.FRANCHISE_HEADER -> return context.deserialize<Any>(paramJsonElement,
                        FranchiseHeaderComponent::class.java) as AbstractComponent
                AbstractComponent.GRID -> return context.deserialize<Any>(paramJsonElement,
                        GridComponent::class.java) as AbstractComponent
                AbstractComponent.LANE -> return context.deserialize<Any>(paramJsonElement,
                        LaneComponent::class.java) as AbstractComponent
                AbstractComponent.LOAD_MORE -> return context.deserialize<Any>(paramJsonElement,
                        LoadMoreComponent::class.java) as AbstractComponent
                AbstractComponent.SPOTLIGHT_HEADER -> return context.deserialize<Any>(paramJsonElement,
                        SpotlightHeaderComponent::class.java) as AbstractComponent
                AbstractComponent.TABS -> return context.deserialize<Any>(paramJsonElement,
                        TabsComponent::class.java) as AbstractComponent
                AbstractComponent.LIVE_HEADER -> return context.deserialize<Any>(paramJsonElement,
                        LiveHeaderComponent::class.java) as AbstractComponent
                AbstractComponent.NOW_PLAYING -> return context.deserialize<Any>(paramJsonElement,
                        NowPlayingComponent::class.java) as AbstractComponent
                AbstractComponent.PICKER -> return context.deserialize<Any>(paramJsonElement,
                        PickerComponent::class.java) as AbstractComponent
                else -> {
                    return null
                }
            }
        }
        return null
    }
}