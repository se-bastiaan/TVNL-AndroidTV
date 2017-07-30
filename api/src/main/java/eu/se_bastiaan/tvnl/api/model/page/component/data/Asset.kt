package eu.se_bastiaan.tvnl.api.model.page.component.data

import eu.se_bastiaan.tvnl.api.model.entity.*
import eu.se_bastiaan.tvnl.api.model.epg.Schedule
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.util.*

class Asset(broadcasters: List<String>, description: String, id: String,
            images: Map<String, Image>, isOnlyOnNpoPlus: Boolean, links: Map<String, Link>,
            onDemand: Boolean, title: String, type: String,
            val ageRating : String?, val availability: Availability?,
            val broadcastDate : ZonedDateTime, val channel : String,
            val collections : List<Collection>, val contentRatings : List<String> = ArrayList(),
            val duration : Int, val episodeNumber : Int = 0,
            val episodeTitle : String = "", val net : String,
            val premiumAvailability: Availability?, val seasions : List<ItemReference>,
            val shareText : String, val state : String,
            val subtitles : List<Subtitle> = ArrayList(), val url : String) : AbstractAsset(
        broadcasters, description, id, images, isOnlyOnNpoPlus, links, onDemand, title, type) {

    var upcomingSchedule : Schedule? = null
    var overrideMediaId : String? = null

    private fun hasAvailability(paramAvailability: Availability, paramZonedDateTime: ZonedDateTime): Boolean {
        return paramZonedDateTime.isAfter(paramAvailability.from) && paramZonedDateTime.isBefore(paramAvailability.to)
    }

    val ageAndContentRatings: List<String>
        get() {
            val returnList : ArrayList<String> = ArrayList()
            if (ageRating != null && !ageRating.isEmpty()) {
                returnList.add(ageRating)
            }
            returnList.addAll(contentRatings)
            return returnList
        }

    fun hasPremiumAvailability(dateTime: ZonedDateTime): Boolean {
        return premiumAvailability != null &&
                hasAvailability(premiumAvailability, dateTime)
    }

    fun hasRegularAvailability(dateTime: ZonedDateTime): Boolean {
        return availability != null &&
                hasAvailability(availability, dateTime)
    }

    fun isAvailableNow(hasPremiumAccess: Boolean): Boolean {
        val currentTime = ZonedDateTime.now(ZoneId.of("Europe/Amsterdam"))
        if (isOnlyOnNpoPlus) {
            return hasPremiumAccess && hasPremiumAvailability(currentTime)
        }
        return hasRegularAvailability(currentTime)
    }
}