package swiftcomplete

import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.What3WordsAddressValidatorSwiftComplete
import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.SwiftCompleteAPIService
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddressSwiftComplete
import com.what3words.addressvalidator.javawrapper.utils.Either
import utils.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


internal class W3WAddressValidatorSwiftCompleteTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var swiftCompleteAPIService: SwiftCompleteAPIService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.url("localHost/")
        swiftCompleteAPIService =
            Network.createRetrofitTestInstance(
                mockWebServer.url("places/").toString()
            )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test that search with valid response body is deserialized correctly`() {
        runTest {
            val searchResponseBody =
                ClassLoader.getSystemResource("filled.count.soap.json").readText()

            val searchResponse = MockResponse()
                .setResponseCode(200)
                .setBody(
                    searchResponseBody
                )

            mockWebServer.enqueue(searchResponse)


            val w3wAddressValidator = What3WordsAddressValidatorSwiftComplete(
                coroutineDispatcher = Dispatchers.IO,
                swiftCompleteApiKey = "",
                swiftCompleteAPIService = swiftCompleteAPIService
            )

            val result = w3wAddressValidator.search("///filled.count.soap")
            assert(result is Either.Right)
            result as Either.Right
            assertEquals(5, result.b.children.size)
            val firstChild = result.b.children[0]
            assertEquals("Alfred Rd", firstChild.primaryAddress)
            assertEquals("London", firstChild.secondaryAddress)
            assert(firstChild is W3WAddressValidatorParentNode)
            firstChild as W3WAddressValidatorParentNode
            assertEquals("V1;Alfred Rd|LONDON|W2;51.521309;-0.194860", firstChild.container)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test that search with empty response body fails`() {
        runTest {
            val jsonSearchResponse = MockResponse()
                .setResponseCode(300)
                .setBody("")

            mockWebServer.enqueue(jsonSearchResponse)

            val w3wAddressValidator = What3WordsAddressValidatorSwiftComplete(
                coroutineDispatcher = Dispatchers.IO,
                swiftCompleteApiKey = "",
                swiftCompleteAPIService = swiftCompleteAPIService
            )
            val searchRequest = w3wAddressValidator.search("///index.home.raft")
            assert(searchRequest is Either.Left)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test that calling the list function `() {
        runTest {
            val firstListResponseBody =
                ClassLoader.getSystemResource("filled.count.soap.firstlist.json").readText()

            val listResponse = MockResponse()
                .setResponseCode(200)
                .setBody(
                    firstListResponseBody
                )
            mockWebServer.enqueue(listResponse)

            val w3wAddressValidator = What3WordsAddressValidatorSwiftComplete(
                coroutineDispatcher = Dispatchers.IO,
                swiftCompleteApiKey = "",
                swiftCompleteAPIService = swiftCompleteAPIService
            )

            val listResult = w3wAddressValidator.list(
                node = W3WAddressValidatorParentNode(
                    primaryAddress = "Alfred Rd",
                    secondaryAddress = "London",
                    threeWordAddress = "///filled.count.soap",
                    container = "V1;Alfred Rd|LONDON|W2;51.521309;-0.194860",
                    parent = null
                )
            )
            assert(listResult is Either.Right)
            listResult as Either.Right
            assert(listResult.b.isNotEmpty())
            assertEquals(5, listResult.b.size)
            val firstChild = listResult.b[0]
            assertEquals("The Harbour Club, 1 Alfred Rd", firstChild.primaryAddress)
            assert(!firstChild.isContainer)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test that get info returns expected address`() {
        runTest {
            val infoResponseBody =
                ClassLoader.getSystemResource("filled.count.soap.returnedaddress.json").readText()
            val infoResponse = MockResponse()
                .setResponseCode(200)
                .setBody(
                    infoResponseBody
                )
            mockWebServer.enqueue(infoResponse)

            val w3wAddressValidator = What3WordsAddressValidatorSwiftComplete(
                coroutineDispatcher = Dispatchers.IO,
                swiftCompleteApiKey = "",
                swiftCompleteAPIService = swiftCompleteAPIService
            )

            val infoResult = w3wAddressValidator.info(
                node = W3WAddressValidatorLeafNode(
                    primaryAddress = "Alfred Rd",
                    secondaryAddress = "London",
                    threeWordAddress = "///filled.count.soap",
                    parent = null
                )
            )
            assert(infoResult is Either.Right)
            infoResult as Either.Right
            val address = infoResult.b
            assert(address is W3WStreetAddressSwiftComplete)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}