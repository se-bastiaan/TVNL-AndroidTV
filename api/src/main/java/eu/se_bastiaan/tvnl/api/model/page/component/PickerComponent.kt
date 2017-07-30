package eu.se_bastiaan.tvnl.api.model.page.component

import com.google.gson.annotations.SerializedName
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter
import java.util.*


class PickerComponent(data: Data, filter: Filter, isPlaceHolder: Boolean, links: Map<String, Link>,
                      mediaTarget: String, target: String, targetedBy: List<String>,
                      targets: List<String>, title: String?, id: String,
                      @SerializedName("edit") val editableComponents: List<String>,
                      val options: List<Option>) : AbstractComponent(data, filter, isPlaceHolder,
        links, mediaTarget, target, targetedBy, targets, title, AbstractComponent.PICKER, id) {

    fun getAllTargets(): List<String> {
        val returnList = ArrayList<String>()
        for (option in options) {
            returnList.addAll(option.components)
        }
        return returnList
    }


    data class Option(val components: List<String>, val default: Boolean,
                      val isSelected: Boolean, val title: String)

}