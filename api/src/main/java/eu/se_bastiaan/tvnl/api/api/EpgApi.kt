package eu.se_bastiaan.tvnl.api.api

import eu.se_bastiaan.tvnl.api.model.epg.EpgModel
import eu.se_bastiaan.tvnl.api.service.EpgService
import io.reactivex.Single

class EpgApi internal constructor(private val epgService: EpgService) {


    fun getEpg(date: String): Single<EpgModel> {
        return epgService.getEpg(date)
    }

    fun getEpg(date: String, type: String): Single<EpgModel> {
        return epgService.getEpg(date, type)
    }

}
