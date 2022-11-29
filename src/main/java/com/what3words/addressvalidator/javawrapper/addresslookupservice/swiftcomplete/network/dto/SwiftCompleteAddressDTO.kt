package com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.dto


data class SwiftCompleteAddressDTO(
    val primary: Address?,
    val secondary: Address?,
    val type: String?,
    val isContainer: Boolean?,
    val container: String?,
    val realDistance: Double?,
    val distance: Distance?,
    val populatedRecord: PopulatedRecord?,
    val record: Record?
) {
    data class Address(
        val text: String?,
        val highlights: List<Int>?
    )

    data class Distance(
        val units: String?,
        val measurement: Float?,
        val type: String?,
        val geometry: Geometry?
    ) {
        data class Geometry(
            val centre: Centre?
        ) {
            data class Centre(
                val lat: Double?,
                val lon: Double?
            )
        }
    }

    data class PopulatedRecord(
        val lines: List<String>?,
        val label: String?
    )

    data class Record(
        val what3words: What3Words?
    ) {
        data class What3Words(
            val text: String?,
            val highlights: List<Int>?
        )
    }
}
