package com.what3words.addressvalidator.javawrapper.model.address

data class W3WAddressValidatorRootNode(
    override val threeWordAddress: String,
    override val children: MutableList<W3WAddressValidatorNode>
) : W3WAddressValidatorAncestorNode() {
    override val primaryAddress: String = threeWordAddress
    override val secondaryAddress: String = threeWordAddress
    override val isContainer: Boolean = true
    override val container: String = ""
    override val parent: W3WAddressValidatorAncestorNode? = null
}