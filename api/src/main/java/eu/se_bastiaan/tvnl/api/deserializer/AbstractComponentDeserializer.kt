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