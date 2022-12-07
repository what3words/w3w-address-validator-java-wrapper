package com.what3words.addressvalidator.javawrapper

import com.what3words.addressvalidator.javawrapper.addresslookupservice.AddressLookUpService
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.utils.Either
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * Class that allows you to call [What3WordsAddressValidator] functions from JAVA
 * **/
class What3WordsAddressValidatorJavaWrapper(addressLookUpService: AddressLookUpService) {

    private val what3WordsAddressValidator: What3WordsAddressValidator =
        What3WordsAddressValidatorV1(addressLookUpService = addressLookUpService)

    @OptIn(DelicateCoroutinesApi::class)
    fun search(near: String): CompletableFuture<Either<W3WAddressValidatorError, W3WAddressValidatorRootNode>> =
        GlobalScope.future { what3WordsAddressValidator.search(near = near) }

    @OptIn(DelicateCoroutinesApi::class)
    fun list(node: W3WAddressValidatorParentNode): CompletableFuture<Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>>> =
        GlobalScope.future {
            what3WordsAddressValidator.list(node = node)
        }

    @OptIn(DelicateCoroutinesApi::class)
    fun info(node: W3WAddressValidatorLeafNode): CompletableFuture<Either<W3WAddressValidatorError, W3WStreetAddress>> =
        GlobalScope.future {
            what3WordsAddressValidator.info(node = node)
        }
}