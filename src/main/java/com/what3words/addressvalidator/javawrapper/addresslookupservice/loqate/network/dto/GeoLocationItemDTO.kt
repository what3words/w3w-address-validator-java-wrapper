package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto

import com.squareup.moshi.Json

internal data class GeoLocationItemDTO(
    @Json(name = "Id") val id: String?,
    @Json(name = "Type") val type: String?,
    @Json(name = "Text") val text: String?,
    @Json(name = "Description") val description: String?,
    @Json(name = "Latitude") val latitude: String?,
    @Json(name = "Longitude") val longitude: String?
)
