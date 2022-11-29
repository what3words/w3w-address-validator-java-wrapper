package com.what3words.addressvalidator.javawrapper.model.address

sealed class W3WAddressValidatorAncestorNode : W3WAddressValidatorNode() {
    abstract val children: MutableList<W3WAddressValidatorNode>
    abstract val container: String
}