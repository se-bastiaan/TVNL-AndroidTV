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