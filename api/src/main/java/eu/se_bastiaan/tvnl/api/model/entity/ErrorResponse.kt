package eu.se_bastiaan.tvnl.api.model.entity

data class ErrorResponse(
        val code : String, val message : String, val file : String
)