package com.what3words.addressvalidator.javawrapper

import com.what3words.addressvalidator.javawrapper.addresslookupservice.AddressLookUpService
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.AddressLookUpServiceUtils.getAddressValidator
import com.what3words.addressvalidator.javawrapper.utils.Either


class What3WordsAddressValidatorV1(addressLookUpService: AddressLookUpService) : What3WordsAddressValidator {
    private val internal: What3WordsAddressValidator = addressLookUpService.getAddressValidator()
    override suspend fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> =
        internal.search(near)


    override suspend fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> =
        internal.list(node)

    override suspend fun info(node: W3WAddressValidatorLeafNode): Either<W3WAddressValidatorError, W3WStreetAddress> =
        internal.info(node)
}


