package eu.se_bastiaan.tvnl.api.api

import eu.se_bastiaan.tvnl.api.model.page.component.menu.NpoMenu
import eu.se_bastiaan.tvnl.api.service.MenuService
import io.reactivex.Single

class MenuApi internal constructor(internal val menuService : MenuService) {

    fun getMenu() : Single<NpoMenu> {
        return menuService.getMenu()
    }

}