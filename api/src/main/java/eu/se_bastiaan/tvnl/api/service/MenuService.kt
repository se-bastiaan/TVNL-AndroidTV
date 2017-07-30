package eu.se_bastiaan.tvnl.api.service

import eu.se_bastiaan.tvnl.api.model.page.component.menu.NpoMenu
import io.reactivex.Single
import retrofit2.http.GET

internal interface MenuService {

    @GET("menu")
    fun getMenu(): Single<NpoMenu>

}