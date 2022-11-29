package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.mappers

import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorAncestorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response.Data8SearchOrDrillResponse

internal fun Data8SearchOrDrillResponse.Data8ResultItem.toW3WAddressValidatorNode(
    parentNode: W3WAddressValidatorAncestorNode
): W3WAddressValidatorNode {
    val transformedLabel = label?.split(regex = Regex(","), limit = 2)
    val primaryAddress: String? = transformedLabel?.get(0)?.trim()
    val secondaryAddress: String? = if ((transformedLabel?.size ?: -1) > 1
    ) transformedLabel?.get(1)?.trim() else null
    return when (container) {
        true -> {
            W3WAddressValidatorParentNode(
                primaryAddress = primaryAddress ?: "",
                secondaryAddress = secondaryAddress ?: "",
                threeWordAddress = parentNode.threeWordAddress,
                container = value ?: "",
                parent = parentNode
            )
        }
        else -> {
            W3WAddressValidatorLeafNode(
                primaryAddress = primaryAddress ?: "N/A",
                secondaryAddress = secondaryAddress ?: "N/A",
                threeWordAddress = parentNode.threeWordAddress,
                parent = parentNode,
                id = value ?: ""
            )
        }
    }
}