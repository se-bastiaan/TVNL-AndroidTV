package eu.se_bastiaan.tvnl.api.model.page.component.spotlightheader

import com.google.gson.annotations.SerializedName
import eu.se_bastiaan.tvnl.api.model.entity.Image
import eu.se_bastiaan.tvnl.api.model.entity.Link
import eu.se_bastiaan.tvnl.api.model.page.component.data.EmbeddedAsset


data class SpotlightHeaderSlide(val buttonText : String = "", val description : String = "",
                                val embeddedAsset: EmbeddedAsset? = null,
                                val images : Map<String, Image> = HashMap(),
                                @SerializedName("_links") val links : Map<String, Link> = HashMap(),
                                val title : String = "")