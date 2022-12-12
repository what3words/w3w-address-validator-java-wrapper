package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8


import com.what3words.addressvalidator.javawrapper.What3WordsAddressValidator
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.mappers.toW3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.mappers.toW3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.address.*
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.Either
import com.what3words.addressvalidator.javawrapper.utils.makeRequest
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.Data8APIService
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.request.Data8DrillDownAndRetrieveRequestBody
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.request.Data8SearchRequestBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/** when we decide to support other countries, we shall provide a function in the [What3WordsAddressValidator] that lets clients specify options such as country**/
private const val COUNTRY = "GB"

internal class What3WordsAddressValidatorData8(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val data8APIKey: String,
    private val data8APIService: Data8APIService
) : What3WordsAddressValidator {
    // The session id that was received in the previous response. This improves efficiency and lookup speeds. This should be included in any subsequent calls to the Search endpoint
    private var sessionID: String? = null

    override suspend fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> {
        return withContext(coroutineDispatcher) {
            val searchRequestResult = makeRequest {
                data8APIService.search(
                    key = data8APIKey,
                    requestBody = Data8SearchRequestBody(
                        search = near,
                        country = COUNTRY,
                        sessionID = sessionID ?: ""
                    )
                )
            }

            when (searchRequestResult) {
                is Either.Right -> {
                    if (searchRequestResult.b.status?.success == true) {
                        sessionID = searchRequestResult.b.sessionID
                        // make drill down request on first search result
                        val searchResults = searchRequestResult.b.results
                        if (!searchResults.isNullOrEmpty()) {
                            val children: MutableList<W3WAddressValidatorNode> = mutableListOf()
                            val rootNode = W3WAddressValidatorRootNode(
                                threeWordAddress = near,
                                children = children,
                                container = searchRequestResult.b.results[0].value!!
                            )
                            val drillDownRequest = makeDrillDownRequest(
                                parentNode = rootNode,
                                country = COUNTRY
                            )
                            when (drillDownRequest) {
                                is Either.Right -> {
                                    children.addAll(drillDownRequest.b)
                                    Either.Right(rootNode)
                                }
                                is Either.Left -> drillDownRequest
                            }
                        } else Either.Left(W3WAddressValidatorError(message = "results empty"))
                    } else {

                        Either.Left(
                            W3WAddressValidatorError(
                                message = searchRequestResult.b.status?.errorMessage ?: ""
                            )
                        )
                    }
                }
                is Either.Left -> {
                    searchRequestResult
                }
            }
        }
    }

    override suspend fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> {
        return makeDrillDownRequest(
            parentNode = node,
            country = COUNTRY
        )
    }

    override suspend fun info(node: W3WAddressValidatorLeafNode): Either<W3WAddressValidatorError, W3WStreetAddress> {
        return withContext(coroutineDispatcher) {
            val retrieveRequest = makeRequest {
                data8APIService.retrieve(
                    key = data8APIKey,
                    requestBody = Data8DrillDownAndRetrieveRequestBody(
                        id = node.id,
                        country = COUNTRY
                    )
                )
            }

            when (retrieveRequest) {
                is Either.Right -> {
                    if (retrieveRequest.b.status?.success == true) {
                        val streetAddress =
                            retrieveRequest.b.result?.toW3WStreetAddress(threeWordAddress = node.threeWordAddress)
                        if (streetAddress != null) {
                            Either.Right(streetAddress)
                        } else Either.Left(W3WAddressValidatorError(message = "No result"))
                    } else Either.Left(
                        W3WAddressValidatorError(
                            message = retrieveRequest.b.status?.errorMessage ?: ""
                        )
                    )
                }
                is Either.Left -> retrieveRequest
            }
        }
    }

    private suspend fun makeDrillDownRequest(
        parentNode: W3WAddressValidatorAncestorNode,
        country: String
    ): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> {
        return withContext(coroutineDispatcher) {
            val drillDownRequest = makeRequest {
                data8APIService.drillDown(
                    key = data8APIKey,
                    requestBody = Data8DrillDownAndRetrieveRequestBody(
                        id = parentNode.container,
                        country = country
                    )
                )
            }
            when (drillDownRequest) {
                is Either.Right -> {
                    if (drillDownRequest.b.status?.success == true) {
                        Either.Right(drillDownRequest.b.results?.map {
                            it.toW3WAddressValidatorNode(
                                parentNode = parentNode
                            )
                        } ?: listOf())
                    } else {
                        Either.Left(
                            W3WAddressValidatorError(
                                message = drillDownRequest.b.status?.errorMessage ?: "N/A"
                            )
                        )
                    }
                }
                is Either.Left -> drillDownRequest
            }
        }
    }
}