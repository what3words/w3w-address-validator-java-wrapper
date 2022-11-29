package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response

import com.squareup.moshi.Json

data class Dat8ResponseStatus(
    @Json(name = "Success") val success: Boolean?,
    @Json(name = "ErrorMessage") val errorMessage: String?,
    @Json(name = "CreditsRemaining") val creditsRemaining: Int?
)