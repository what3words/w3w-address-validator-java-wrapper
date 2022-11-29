package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto

import com.squareup.moshi.Json

internal data class LoqateResponseDTO<T>(
    @Json(name = "Items") val items: List<T>
)
