package com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.mappers


import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.dto.SwiftCompleteAddressDTO
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorAncestorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddressSwiftComplete


internal fun SwiftCompleteAddressDTO.toW3WAddressValidatorNode(
    parentNode: W3WAddressValidatorAncestorNode
): W3WAddressValidatorNode {
    return when (isContainer) {
        true -> {
            W3WAddressValidatorParentNode(
                primaryAddress = primary?.text ?: "",
                secondaryAddress = secondary?.text ?: "",
                threeWordAddress = record?.what3words?.text?.removePrefix("///") ?: "",
                container = container ?: "",
                parent = parentNode
            )
        }
        else -> {
            W3WAddressValidatorLeafNode(
                primaryAddress = primary?.text ?: "",
                secondaryAddress = secondary?.text ?: "",
                threeWordAddress = record?.what3words?.text?.removePrefix("///") ?: "",
                parent = parentNode
            )
        }
    }
}


internal fun SwiftCompleteAddressDTO.toW3WStreetAddress(): W3WStreetAddress {
    return W3WStreetAddressSwiftComplete(
        mWords = record?.what3words?.text,
        mDescription = populatedRecord?.label,
        mPrimary = primary?.toStreetAddress(),
        mSecondary = secondary?.toStreetAddress(),
        type = type,
        isContainer = isContainer,
        realDistance = realDistance,
        distance = distance?.toStreetAddress(),
        populatedRecord = W3WStreetAddressSwiftComplete.PopulatedRecord(
            lines = populatedRecord?.lines,
            label = populatedRecord?.label
        ),
        record = W3WStreetAddressSwiftComplete.Record(
            what3words = W3WStreetAddressSwiftComplete.Record.What3Words(
                text = record?.what3words?.text,
                highlights = record?.what3words?.highlights
            )
        )
    )
}

private fun SwiftCompleteAddressDTO.Address.toStreetAddress(): W3WStreetAddressSwiftComplete.Address {
    return W3WStreetAddressSwiftComplete.Address(
        text = text,
        highlights = highlights
    )
}

private fun SwiftCompleteAddressDTO.Distance.toStreetAddress(): W3WStreetAddressSwiftComplete.Distance {
    return W3WStreetAddressSwiftComplete.Distance(
        units = units,
        measurement = measurement,
        type = type,
        geometry = W3WStreetAddressSwiftComplete.Distance.Geometry(
            centre = W3WStreetAddressSwiftComplete.Distance.Geometry.Centre(
                lat = geometry?.centre?.lat,
                lon = geometry?.centre?.lon
            )
        )
    )
}

