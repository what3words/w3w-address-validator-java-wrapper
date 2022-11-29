package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response

import com.squareup.moshi.Json

/**
 * @param sessionID The session id that was received in the previous response. This improves efficiency and lookup speeds. This should be included in any subsequent calls to the Search endpoint
 * */
internal data class Data8SearchOrDrillResponse(
    @Json(name = "Status") val status: Dat8ResponseStatus?,
    @Json(name = "Results") val results: List<Data8ResultItem>?,
    @Json(name = "Count") val count: Int?,
    @Json(name = "SessionID") val sessionID: String?
) {
    /**
     * @param label the value to show to the user
     * @param value a unique identifier for the match that can be supplied to the DrillDown or Retrieve endpoints
     * @param container indicates if this match is a container for other addresses (e.g. a town, street etc.). If this is true, the value should be supplied to the DrillDown endpoint to get the details of the addresses in the container record. If it is false, the value should be supplied to the Retrieve endpoint to get the full details of the address.
     * **/
    data class Data8ResultItem(
        val label: String?,
        val value: String?,
        val container: Boolean?,
        val items: Int?
    )

}
