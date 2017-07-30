package eu.se_bastiaan.tvnl.api.api

import eu.se_bastiaan.tvnl.api.model.page.component.Data
import eu.se_bastiaan.tvnl.api.service.ComponentService
import io.reactivex.Single

class ComponentApi internal constructor(private val componentService: ComponentService) {

    fun getComponentData(url : String) : Single<Data> {
        return componentService.getComponentData(url)
    }

}