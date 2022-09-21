package com.what3words.addressvalidator.javawrapper.model.address

data class W3WAddressValidatorLeafNode(
    override val primaryAddress: String,
    override val secondaryAddress: String,
    override val threeWordAddress: String,
    override val parent: W3WAddressValidatorAncestorNode?
) : W3WAddressValidatorNode() {
    override val isContainer: Boolean = false
}
