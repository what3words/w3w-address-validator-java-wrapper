package com.what3words.addressvalidator.javawrapper

import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.Either
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.address.info.W3WStreetAddress

class W3WAddressValidatorV1 : W3WAddressValidator {

    override fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> {
        TODO()
    }

    override fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> {
        TODO()
    }


    override fun info(node: W3WAddressValidatorLeafNode): Either<W3WAddressValidatorError, W3WStreetAddress> {
        TODO()
    }

    override fun cancel() {
        TODO()
    }
}