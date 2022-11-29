package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.mappers

import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response.Data8RetrieveResponse
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddressData8

internal fun Data8RetrieveResponse.Data8RetrieveItem.toW3WStreetAddress(
    threeWordAddress: String
): W3WStreetAddress {
    val label: String? = address?.lines?.customJoinToString()
    val transformedLabel: List<String>? = label?.split(regex = Regex(pattern = ","), limit = 2)
    return W3WStreetAddressData8(
        threeWordsAddress = threeWordAddress,
        address = address?.toStreetAddress(),
        rawAddress = rawAddress?.toStreetAddress(),
        mPrimary = transformedLabel?.get(0),
        mSecondary = if ((transformedLabel?.size ?: -1) > 1
        ) transformedLabel?.get(1) else null,
        label = label
    )
}

private fun List<String>.customJoinToString(): String {
    val stringBuilder = StringBuilder()

    forEachIndexed { index, value ->
        if (value.isNotBlank()) {
            stringBuilder.append(value.trim())
            if (index != size - 1) stringBuilder.append(", ")
        }
    }

    return stringBuilder.toString()
}

private fun Data8RetrieveResponse.Data8RetrieveItem.Address.toStreetAddress(): W3WStreetAddressData8.Address {
    return W3WStreetAddressData8.Address(
        lines = lines
    )
}

private fun Data8RetrieveResponse.Data8RetrieveItem.RawAddress.toStreetAddress(): W3WStreetAddressData8.RawAddress {
    return W3WStreetAddressData8.RawAddress(
        organisation = organization,
        department = department,
        addressKey = addressKey,
        organisationKey = organisationKey,
        postcodeType = postcodeType,
        buildingNumber = buildingNumber,
        subBuildingName = subBuildingName,
        buildingName = buildingName,
        dependentThoroughfareName = dependentThoroughfareName,
        dependentThoroughfareDesc = dependentThoroughfareDesc,
        thoroughfareName = thoroughfareName,
        thoroughfareDesc = thoroughfareDesc,
        doubleDependentLocality = doubleDependentLocality,
        dependentLocality = dependentLocality,
        locality = locality,
        postcode = postcode,
        dps = dps,
        poBox = poBox,
        postalCounty = postalCounty,
        traditionalCounty = traditionalCounty,
        administrativeCounty = administrativeCounty,
        countryISO2 = countryISO2,
        uniqueReference = uniqueReference,
        location = location?.toStreetAddress(),
        additionalData = additionalData?.map { it.toStreetAddress() }
    )
}

private fun Data8RetrieveResponse.Data8RetrieveItem.RawAddress.Location.toStreetAddress(): W3WStreetAddressData8.RawAddress.Location {
    return W3WStreetAddressData8.RawAddress.Location(
        easting = easting,
        northing = northing,
        gridReference = gridReference,
        longitude = longitude,
        latitude = latitude,
        countyCode = countyCode,
        county = county,
        districtCode = districtCode,
        district = district,
        wardCode = wardCode,
        ward = ward,
        country = country
    )
}

private fun Data8RetrieveResponse.Data8RetrieveItem.RawAddress.AdditionalData.toStreetAddress(): W3WStreetAddressData8.RawAddress.AdditionalData {
    return W3WStreetAddressData8.RawAddress.AdditionalData(
        name = name,
        value = value
    )
}