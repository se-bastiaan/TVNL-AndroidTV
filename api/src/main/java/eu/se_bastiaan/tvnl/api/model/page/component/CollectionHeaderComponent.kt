package eu.se_bastiaan.tvnl.api.model.page.component


import eu.se_bastiaan.tvnl.api.model.entity.Collection
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.filter.Filter

class CollectionHeaderComponent(data: Data, filter: Filter,
                                isPlaceHolder: Boolean, links: Map<String, Link>,
                                mediaTarget: String, target: String,
                                targetedBy: List<String>, targets: List<String>,
                                title: String?, id: String, val collection: Collection) :
        AbstractComponent(data, filter, isPlaceHolder, links, mediaTarget,
                target, targetedBy, targets, title, AbstractComponent.COLLECTION_HEADER, id)
