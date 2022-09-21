package com.what3words.addressvalidator.javawrapper.model.address.info


data class W3WStreetAddressSwiftComplete(
        val mWords: String?,
        val mPrimary: Address?,
        val mSecondary: Address?,
        val mDescription: String?,
        val type: String?,
        val isContainer: Boolean?,
        val realDistance: Double?,
        val distance: Distance?,
        val populatedRecord: PopulatedRecord?,
        val record: Record?
) : W3WStreetAddress(
        words = mWords,
        description = mDescription,
        primary = mPrimary?.text,
        secondary = mSecondary?.text
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

    /*** this object contains the index that can be used to query [W3WStreetAddressSwiftComplete.PopulatedRecord.lines] indexes and
     * it provides an understanding of what each value at a particular index stands for
     *  **/
    companion object PopulatedRecordLinesIndex {
        const val ADDRESS = 0
        const val STREET = 1
        const val LOCALITY = 2
        const val CITY = 3
        const val POST_CODE = 4
        const val COUNTRY = 5 // value for country is at index 5
    }

}