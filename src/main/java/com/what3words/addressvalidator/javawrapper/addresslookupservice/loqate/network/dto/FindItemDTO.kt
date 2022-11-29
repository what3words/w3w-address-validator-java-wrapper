package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto

import com.squareup.moshi.Json


internal data class FindItemDTO(
    @Json(name = "Id") val id: String?,
    @Json(name = "Type") val type: String?,
    @Json(name = "Text") val text: String?,
    @Json(name = "Highlight") val highlight: String?,
    @Json(name = "Description") val description: String?
)
