package eu.se_bastiaan.tvnl.api.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.threeten.bp.ZoneId

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalField
import org.threeten.bp.temporal.TemporalQuery

import java.lang.reflect.Type

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {

    internal val formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("Europe/Amsterdam"))

    override fun serialize(paramZonedDateTime: ZonedDateTime, paramType: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(formatter.format(paramZonedDateTime))
    }

    @Throws(JsonParseException::class)
    override fun deserialize(paramJsonElement: JsonElement, paramType: Type, context: JsonDeserializationContext): ZonedDateTime {
        return ZonedDateTime.parse(paramJsonElement.asString, formatter)
    }

}