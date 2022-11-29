package com.what3words.addressvalidator.javawrapper

import com.what3words.addressvalidator.javawrapper.utils.Either
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError

interface What3WordsAddressValidator {
    /**searches near a three word address
     *
     *@param near: the three word address to search near
     *@return Either [W3WAddressValidatorRootNode] if the call was successful or
     *  [W3WAddressValidatorError] in the event of a failure
     **/
    suspend fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode>


    /**
     * returns children (or sub addresses) under [node]
     * @param node to fetch its children
     * @return Either [List<[W3WAddressValidatorNode]>] if call was successful or [W3WAddressValidatorError] when list operation fails
     * **/
    suspend fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>>

    /**
     * returns the [W3WStreetAddress] for a leaf node
     * @param node the node to make the detail/info call on
     **/
    suspend fun info(
        node: W3WAddressValidatorLeafNode
    ): Either<W3WAddressValidatorError, W3WStreetAddress>
}