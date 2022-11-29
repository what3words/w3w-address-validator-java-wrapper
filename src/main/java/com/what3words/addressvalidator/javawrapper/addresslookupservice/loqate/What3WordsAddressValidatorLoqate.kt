package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate

import com.what3words.addressvalidator.javawrapper.What3WordsAddressValidator
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.Either
import com.what3words.addressvalidator.javawrapper.utils.makeRequest
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal.LoqateTree
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal.LoqateTreeImpl
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.mappers.toW3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.mappers.toW3WStreetAddress
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.LoqateAPIService
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.FindItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.GeoLocationItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.LoqateResponseDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.RetrieveItemDTO
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.javawrapper.What3WordsV3
import com.what3words.javawrapper.response.Coordinates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class What3WordsAddressValidatorLoqate(
    private val what3WordsV3: What3WordsV3,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val loqateApiKey: String,
    private val loqateAPIService: LoqateAPIService,
) : What3WordsAddressValidator {

    private val internalLoqateTree: LoqateTree = LoqateTreeImpl()
    override suspend fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> {
        /*** Algorithm
         * 1. convert 3wa to lat long
         * 2. query loqate for geolocation item list
         * 3. for each of the item in the geolocation item list, query loqate find by passing the geolocationItem text and description concatenated as a single string
         *    3.1. split the findItem text attribute into a list of string by using the commas(,) within the text as a delimiter
         *    3.2. add the list of words in 3.1 as nodes in the internal loqate tree
         * **/
        return withContext(coroutineDispatcher) {
            internalLoqateTree.clear()
            val coordinates: Coordinates? =
                what3WordsV3.convertToCoordinates(near).execute().coordinates
            if (coordinates != null) {
                val geoLocateResult: Either<W3WAddressValidatorError, LoqateResponseDTO<GeoLocationItemDTO>> =
                    makeRequest {
                        loqateAPIService.geoLocate(
                            key = loqateApiKey,
                            lat = coordinates.lat,
                            long = coordinates.lng
                        )
                    }
                when (geoLocateResult) {
                    is Either.Right -> {
                        val geoLocationItems = geoLocateResult.b.items
                        val findItemJobs = mutableListOf<Job>()
                        for (item in geoLocationItems) {
                            findItemJobs.add(launch {
                                makeFindRequest(geoLocationItemDTO = item)
                            })
                        }
                        findItemJobs.joinAll()
                        // PRUNE TREE TO REDUCE THE DEPTH OF TREE
                        internalLoqateTree.prune()
                        val rootNode = W3WAddressValidatorRootNode(
                            threeWordAddress = near,
                            children = mutableListOf()
                        )
                        rootNode.children.apply {
                            addAll(internalLoqateTree.getRoot().children().map {
                                it.toW3WAddressValidatorNode(
                                    parent = rootNode,
                                    threeWordAddress = near
                                )
                            })
                        }
                        Either.Right(rootNode)
                    }
                    is Either.Left -> {
                        Either.Left(geoLocateResult.a)
                    }
                }
            } else {
                Either.Left(W3WAddressValidatorError("Could not convert three word address to coordinates"))
            }
        }
    }

    private suspend fun makeFindRequest(geoLocationItemDTO: GeoLocationItemDTO): Either<W3WAddressValidatorError, Unit> {
        val findResult: Either<W3WAddressValidatorError, LoqateResponseDTO<FindItemDTO>> =
            makeRequest {
                loqateAPIService.find(
                    key = loqateApiKey,
                    text = "${geoLocationItemDTO.text} ${geoLocationItemDTO.description}"
                )
            }
        return when (findResult) {
            is Either.Right -> {
                if (findResult.b.items.isNotEmpty() && !findResult.b.items.first().text.isNullOrBlank()) {
                    val findItem = findResult.b.items.first()
                    val addressWords =
                        findItem.text!!.split(",")
                    internalLoqateTree.addNodes(
                        addressWords,
                        description = findItem.description ?: "",
                        id = findItem.id ?: ""
                    )
                    Either.Right(Unit)
                } else {
                    Either.Left(W3WAddressValidatorError("Inconsistent data format"))
                }
            }
            is Either.Left -> {
                Either.Left(findResult.a)
            }
        }
    }

    override suspend fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> {
        /**
         * 1. Get to the root node from the current node param. On each iteration add the primary address of that node into a list
         * 2. Use the list from step 1, to traverse through the internal loqate tree to fetch the children of the node passed as param
         * **/
        // reconstruct address from node to root
        return withContext(coroutineDispatcher) {
            val reconstructedAddress = reconstructAddress(node = node)
            val children = internalLoqateTree.getChildrenAt(wordsList = reconstructedAddress)
            Either.Right(b = children.map {
                it.toW3WAddressValidatorNode(node, threeWordAddress = node.threeWordAddress)
            })
        }
    }

    /***
     * Given a node, reconstruct address list from that node to the parent node
     * e.g if original address list is [Flat 8, Hartford House, 35 Tavistock Crescent] and the current node has a
     * primary address of Harford House, the reconstructed address from current node down to root should be
     * [Hartford House, 35 Tavistock Crescent]
     * ***/
    private fun reconstructAddress(node: W3WAddressValidatorNode): List<String> {
        var currentNode: W3WAddressValidatorNode? = node
        val reconstructedWordsList: MutableList<String> = mutableListOf()
        while (currentNode?.parent != null) {
            reconstructedWordsList.add(currentNode.primaryAddress)
            currentNode = currentNode.parent
        }
        return reconstructedWordsList
    }

    override suspend fun info(node: W3WAddressValidatorLeafNode): Either<W3WAddressValidatorError, W3WStreetAddress> {
        /**
         * get loqate node equivalent of [node] add query loqate for more info about address
         * **/
        val reconstructedAddress: List<String> = reconstructAddress(node = node)
        val leafNode = internalLoqateTree.getNodeAt(wordsList = reconstructedAddress)
        val retrieveResult: Either<W3WAddressValidatorError, LoqateResponseDTO<RetrieveItemDTO>> =
            makeRequest {
                loqateAPIService.retrieve(
                    key = loqateApiKey,
                    id = leafNode.id
                )
            }
        return when (retrieveResult) {
            is Either.Right -> {
                Either.Right(
                    retrieveResult.b.items[0].toW3WStreetAddress(
                        leafNode = leafNode,
                        threeWordAddress = node.threeWordAddress
                    )
                )
            }
            is Either.Left -> {
                Either.Left(retrieveResult.a)
            }
        }
    }
}