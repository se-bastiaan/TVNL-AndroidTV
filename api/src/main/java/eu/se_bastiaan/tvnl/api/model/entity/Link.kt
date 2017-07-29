package eu.se_bastiaan.tvnl.api.model.entity

import android.support.annotation.StringDef

data class Link(@Type val href: String) {
    companion object {
        @StringDef(DELETE, FIRST, LAST, NEXT, PAGE, PREVIOUS,
                PROMO, PREVIOUS, SELF, WEB)
        @Retention(AnnotationRetention.SOURCE)
        annotation internal class Type

        const val DELETE = "delete"
        const val FIRST = "first"
        const val LAST = "last"
        const val NEXT = "next"
        const val PAGE = "page"
        const val PREVIOUS = "previous"
        const val PROMO = "promo"
        const val SELF = "self"
        const val WEB = "web"
    }
}