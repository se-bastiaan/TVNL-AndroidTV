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

import eu.se_bastiaan.tvnl.api.model.page.Page
import eu.se_bastiaan.tvnl.api.service.PageService
import io.reactivex.Single

class PageApi internal constructor(private val pageService : PageService) {

    fun getAbsolutePage(url: String): Single<Page> {
        return pageService.getAbsolutePage(url)
    }

    fun getAbsolutePage(url: String, pageSize: Int): Single<Page> {
        return pageService.getAbsolutePage(url, pageSize)
    }

    fun getAbsoluteUserPage(url: String, profileId: String): Single<Page> {
        return pageService.getAbsoluteUserPage(url, profileId)
    }

    fun getProfileHome(profileId: String, partyId: String): Single<Page> {
        return pageService.getProfileHome(profileId, partyId)
    }

}