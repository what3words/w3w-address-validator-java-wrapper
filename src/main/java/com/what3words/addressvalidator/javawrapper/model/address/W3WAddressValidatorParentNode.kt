package com.what3words.addressvalidator.javawrapper.model.address

data class W3WAddressValidatorParentNode(
    override val primaryAddress: String,
    override val secondaryAddress: String,
    override val threeWordAddress: String,
    override val container: String,
    override val parent: W3WAddressValidatorAncestorNode?
) : W3WAddressValidatorAncestorNode() {
    override val id: String
        get() = container
    override val isContainer: Boolean = true
    override val children: MutableList<W3WAddressValidatorNode> = mutableListOf()
}
