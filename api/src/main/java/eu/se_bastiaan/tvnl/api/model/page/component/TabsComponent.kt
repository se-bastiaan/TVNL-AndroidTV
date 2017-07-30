package eu.se_bastiaan.tvnl.api.model.page.component

import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.entity.Tab
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter
import java.util.*


class TabsComponent(data: Data, filter: Filter, isPlaceHolder: Boolean, links: Map<String, Link>,
                    mediaTarget: String, target: String, targetedBy: List<String>,
                    targets: List<String>, title: String?, type: String, id: String,
                    val tabs: List<Tab>) : AbstractComponent(data, filter, isPlaceHolder,
        links, mediaTarget, target, targetedBy, targets, title, type, id) {
    internal val tabComponents: ArrayList<List<AbstractComponent>> = ArrayList()

    fun addTabComponentMapping(paramTab: Tab, paramList: List<AbstractComponent>) {
        val i = this.tabs.indexOf(paramTab)
        this.tabComponents.add(i, paramList)
    }

    fun getTabComponents(): List<Pair<Tab, List<AbstractComponent>>> {
        return (0..this.tabs.size - 1)
                .mapTo(ArrayList()) { Pair(this.tabs[it], this.tabComponents[it]) }
    }
}