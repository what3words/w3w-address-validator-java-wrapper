package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response

import com.squareup.moshi.Json

/**
 * @param sessionID The session id that was received in the previous response. This improves efficiency and lookup speeds. This should be included in any subsequent calls to the Search endpoint
 * */
internal data class Data8RetrieveResponse(
    @Json(name = "Status") val status: Dat8ResponseStatus?,
    @Json(name = "Result") val result: Data8RetrieveItem?,
    @Json(name = "Count") val count: Int?,
    @Json(name = "SessionID") val sessionID: String?
) {

    data class Data8RetrieveItem(
        @Json(name = "Address") val address: Address?,
        @Json(name = "RawAddress") val rawAddress: RawAddress?
    ) {
        data class Address(
            @Json(name = "Lines") val lines: List<String>?
        )

        data class RawAddress(
            @Json(name = "Organisation") val organization: String?,
            @Json(name = "Department") val department: String?,
            @Json(name = "AddressKey") val addressKey: Long?,
            @Json(name = "OrganisationKey") val organisationKey: Long?,
            @Json(name = "PostcodeType") val postcodeType: String?,
            @Json(name = "BuildingNumber") val buildingNumber: Long?,
            @Json(name = "SubBuildingName") val subBuildingName: String?,
            @Json(name = "BuildingName") val buildingName: String?,
            @Json(name = "DependentThoroughfareName") val dependentThoroughfareName: String?,
            @Json(name = "DependentThoroughfareDesc") val dependentThoroughfareDesc: String?,
            @Json(name = "ThoroughfareName") val thoroughfareName: String?,
            @Json(name = "ThoroughfareDesc") val thoroughfareDesc: String?,
            @Json(name = "DoubleDependentLocality") val doubleDependentLocality: String?,
            @Json(name = "DependentLocality") val dependentLocality: String?,
            @Json(name = "Locality") val locality: String?,
            @Json(name = "Postcode") val postcode: String?,
            @Json(name = "Dps") val dps: String?,
            @Json(name = "PoBox") val poBox: String?,
            @Json(name = "PostalCounty") val postalCounty: String?,
            @Json(name = "TraditionalCounty") val traditionalCounty: String?,
            @Json(name = "AdministrativeCounty") val administrativeCounty: String?,
            @Json(name = "CountryISO2") val countryISO2: String?,
            @Json(name = "UniqueReference") val uniqueReference: String?,
            @Json(name = "Location") val location: Location?,
            @Json(name = "AdditionalData") val additionalData: List<AdditionalData>?
        ) {
            data class Location(
                @Json(name = "Easting") val easting: Long?,
                @Json(name = "Northing") val northing: Long?,
                @Json(name = "GridReference") val gridReference: String?,
                @Json(name = "Longitude") val longitude: Double?,
                @Json(name = "Latitude") val latitude: Double?,
                @Json(name = "CountyCode") val countyCode: String?,
                @Json(name = "County") val county: String?,
                @Json(name = "DistrictCode") val districtCode: String?,
                @Json(name = "District") val district: String?,
                @Json(name = "WardCode") val wardCode: String?,
                @Json(name = "Ward") val ward: String?,
                @Json(name = "Country") val country: String?
            )

            data class AdditionalData(
                @Json(name = "Name") val name: String?,
                @Json(name = "Value") val value: String?
            )
        }
    }
}


