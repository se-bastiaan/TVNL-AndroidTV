package eu.se_bastiaan.tvnl.api.manager

import eu.se_bastiaan.tvnl.api.model.page.Page
import eu.se_bastiaan.tvnl.api.service.PageService
import io.reactivex.Single

class PageManager(internal val pageService : PageService) {

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