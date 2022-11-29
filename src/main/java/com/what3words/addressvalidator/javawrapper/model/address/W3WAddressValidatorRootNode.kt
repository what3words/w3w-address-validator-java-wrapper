package com.what3words.addressvalidator.javawrapper.model.address

data class W3WAddressValidatorRootNode(
    override val threeWordAddress: String,
    override val children: MutableList<W3WAddressValidatorNode>,
    override val container: String = ""
) : W3WAddressValidatorAncestorNode() {
    override val primaryAddress: String = threeWordAddress
    override val secondaryAddress: String = threeWordAddress
    override val id: String = container
    override val isContainer: Boolean = true
    override val parent: W3WAddressValidatorAncestorNode? = null
}