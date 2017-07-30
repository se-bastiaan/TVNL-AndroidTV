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