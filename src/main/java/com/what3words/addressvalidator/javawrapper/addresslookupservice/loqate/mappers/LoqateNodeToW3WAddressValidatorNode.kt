package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.mappers

import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorAncestorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal.LoqateNode

fun LoqateNode.toW3WAddressValidatorNode(
    parent: W3WAddressValidatorAncestorNode,
    threeWordAddress: String
): W3WAddressValidatorNode =
    if (isParent) {
        W3WAddressValidatorParentNode(
            primaryAddress = primaryAddress,
            secondaryAddress = description,
            threeWordAddress = threeWordAddress,
            container = id,
            parent = parent
        )
    } else {
        W3WAddressValidatorLeafNode(
            primaryAddress = primaryAddress,
            secondaryAddress = description,
            threeWordAddress = threeWordAddress,
            parent = parent
        )
    }
