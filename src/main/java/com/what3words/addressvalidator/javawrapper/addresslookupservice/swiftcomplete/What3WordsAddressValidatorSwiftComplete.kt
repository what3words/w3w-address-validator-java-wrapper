package com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete


import com.what3words.addressvalidator.javawrapper.What3WordsAddressValidator
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.Either
import com.what3words.addressvalidator.javawrapper.utils.makeRequest
import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.mappers.toW3WStreetAddress
import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.mappers.toW3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.SwiftCompleteAPIService
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class What3WordsAddressValidatorSwiftComplete(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val swiftCompleteApiKey: String,
    private val swiftCompleteAPIService: SwiftCompleteAPIService
) : What3WordsAddressValidator {

    override suspend fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> {
        return withContext(coroutineDispatcher) {
            val result = makeRequest {
                swiftCompleteAPIService.search(
                    key = swiftCompleteApiKey,
                    threeWordAddress = formatThreeWordAddress(near)
                )
            }
            when (result) {
                is Either.Right -> {
                    val root = W3WAddressValidatorRootNode(
                        threeWordAddress = near,
                        children = mutableListOf()
                    )
                    Either.Right(
                        root.apply {
                            children.addAll(
                                result.b.map {
                                    it.toW3WAddressValidatorNode(root)
                                }
                            )
                        }
                    )
                }
                is Either.Left -> {
                    result
                }
            }
        }
    }

    override suspend fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> {
        return withContext(coroutineDispatcher) {
            val result = makeRequest {
                swiftCompleteAPIService.list(
                    key = swiftCompleteApiKey,
                    container = node.container,
                    threeWordAddress = formatThreeWordAddress(node.threeWordAddress)
                )
            }

            when (result) {
                is Either.Right -> {
                    Either.Right(result.b.map { it.toW3WAddressValidatorNode(parentNode = node) })
                }
                is Either.Left -> {
                    result
                }
            }
        }
    }

    override suspend fun info(
        node: W3WAddressValidatorLeafNode
    ): Either<W3WAddressValidatorError, W3WStreetAddress> {
        val parentNode = node.parent
        val nodeIndex = parentNode?.children?.indexOf(node) ?: 0
        return withContext(coroutineDispatcher) {
            val result = makeRequest {
                // If the info call was made from the parent node, then perform a search request, and pass the
                // when it is a root node (i.e ancestor without a key) then group the results to be returned by road, and emptyRoad before making the request
                if (parentNode is W3WAddressValidatorRootNode) {
                    swiftCompleteAPIService.search(
                        key = swiftCompleteApiKey,
                        populateIndex = nodeIndex,
                        threeWordAddress = formatThreeWordAddress(node.threeWordAddress),
                    )
                } else {
                    swiftCompleteAPIService.list(
                        key = swiftCompleteApiKey,
                        container = parentNode?.container ?: "",
                        populateIndex = nodeIndex,
                        threeWordAddress = formatThreeWordAddress(node.threeWordAddress),
                    )
                }

            }
            when (result) {
                is Either.Right -> {
                    Either.Right(result.b[nodeIndex].toW3WStreetAddress())
                }
                is Either.Left -> {
                    result
                }
            }
        }
    }

    private fun formatThreeWordAddress(address: String): String {
        return "///${address.trim()}"
    }
}