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
