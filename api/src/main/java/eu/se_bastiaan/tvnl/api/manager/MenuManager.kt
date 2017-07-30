package eu.se_bastiaan.tvnl.api.manager

import eu.se_bastiaan.tvnl.api.model.page.component.menu.NpoMenu
import eu.se_bastiaan.tvnl.api.service.MenuService
import io.reactivex.Single

class MenuManager(val menuService : MenuService) {

    fun getMenu() : Single<NpoMenu> {
        return menuService.getMenu()
    }

}