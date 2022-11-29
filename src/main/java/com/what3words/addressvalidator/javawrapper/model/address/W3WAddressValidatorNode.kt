package com.what3words.addressvalidator.javawrapper.model.address


sealed class W3WAddressValidatorNode {
    abstract val primaryAddress: String
    abstract val secondaryAddress: String
    abstract val isContainer: Boolean
    abstract val threeWordAddress: String
    abstract val id: String
    abstract val parent: W3WAddressValidatorAncestorNode?
}

