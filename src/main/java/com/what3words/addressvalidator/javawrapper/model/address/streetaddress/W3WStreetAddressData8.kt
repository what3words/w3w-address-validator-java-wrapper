package com.what3words.addressvalidator.javawrapper.model.address.streetaddress

import java.io.Serializable


data class W3WStreetAddressData8(
    val threeWordsAddress: String,
    val address: Address?,
    val rawAddress: RawAddress?,
    val mPrimary: String?,
    val mSecondary: String?,
    val label: String?
) : W3WStreetAddress(
    words = threeWordsAddress,
    primary = mPrimary,
    secondary = mSecondary,
    description = label
) {

    data class Address(
        val lines: List<String>?
    ) : Serializable


    data class RawAddress(
        val organisation: String?,
        val department: String?,
        val addressKey: Long?,
        val organisationKey: Long?,
        val postcodeType: String?,
        val buildingNumber: Long?,
        val subBuildingName: String?,
        val buildingName: String?,
        val dependentThoroughfareName: String?,
        val dependentThoroughfareDesc: String?,
        val thoroughfareName: String?,
        val thoroughfareDesc: String?,
        val doubleDependentLocality: String?,
        val dependentLocality: String?,
        val locality: String?,
        val postcode: String?,
        val dps: String?,
        val poBox: String?,
        val postalCounty: String?,
        val traditionalCounty: String?,
        val administrativeCounty: String?,
        val countryISO2: String?,
        val uniqueReference: String?,
        val location: Location?,
        val additionalData: List<AdditionalData>?
    ) : Serializable {
        data class Location(
            val easting: Long?,
            val northing: Long?,
            val gridReference: String?,
            val longitude: Double?,
            val latitude: Double?,
            val countyCode: String?,
            val county: String?,
            val districtCode: String?,
            val district: String?,
            val wardCode: String?,
            val ward: String?,
            val country: String?
        ) : Serializable


        data class AdditionalData(
            val name: String?,
            val value: String?
        ) : Serializable
    }
}
