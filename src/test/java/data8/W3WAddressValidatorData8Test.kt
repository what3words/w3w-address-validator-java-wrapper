package data8

import com.what3words.addressvalidator.javawrapper.What3WordsAddressValidator
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.What3WordsAddressValidatorData8
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.Data8APIService
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddress
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddressData8
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.Either
import data8.utils.FakeData8DataSource
import utils.MockitoHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class W3WAddressValidatorData8Test {
    private val threeWordAddress = "index.home.raft"
    private val data8APIService: Data8APIService = mock(Data8APIService::class.java)
    private val w3wAddressValidatorData8: What3WordsAddressValidator = What3WordsAddressValidatorData8(
        coroutineDispatcher = Dispatchers.IO,
        data8APIKey = "",
        data8APIService = data8APIService
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test happy search path`() = runTest {
        `mock Data8APIService successful search`()
        `mock Data8APIService root level successful drillDown`()

        val searchResult: Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> =
            w3wAddressValidatorData8.search(near = threeWordAddress)
        assert(searchResult is Either.Right)
        searchResult as Either.Right
        with(searchResult.b) {
            assertEquals("index.home.raft", threeWordAddress)
            assertEquals(10, children.size)
            assertEquals(children[0].primaryAddress, "View all nearby streets")
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test unhappy search path due to invalid api key`() = runTest {
        `mock Data8APIService unsuccessful search caused by invalid API Key`()

        val searchResult: Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> =
            w3wAddressValidatorData8.search(near = threeWordAddress)

        assert(searchResult is Either.Left)
        searchResult as Either.Left
        assertEquals(
            "Error parsing JSON: The input does not contain any JSON tokens. Expected the input to start with a valid JSON token, when isFinalBlock is true. Path: \$ | LineNumber: 0 | BytePositionInLine: 0.",
            searchResult.a.message
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test happy list path`() = runTest {
        `mock Data8APIService second level successful drillDown`()

        val listResult: Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> =
            w3wAddressValidatorData8.list(
                node = FakeData8DataSource.dummyParentNode
            )

        assert(listResult is Either.Right)
        listResult as Either.Right
        with(listResult.b) {
            assertEquals(19, size)
            assertEquals("APDA London Ltd", first().primaryAddress)
            assertEquals(
                "PCLejBBO6z5KdLFACP30FmRQ|CC=GB;PAF=2022.9.28.890786;W3W=index.home.raft",
                first().id
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test unhappy list path because of invalid api key`() = runTest {
        `mock Data8APIService unsuccessful drillDown caused by invalid api key`()

        val listResult: Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> =
            w3wAddressValidatorData8.list(
                node = FakeData8DataSource.dummyParentNode
            )

        assert(listResult is Either.Left)
        listResult as Either.Left
        assertEquals(
            "API Key not recognised",
            listResult.a.message
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test happy info path`() = runTest {
        `mock Data8APIService successful retrieve`()

        val infoResult: Either<W3WAddressValidatorError, W3WStreetAddress> =
            w3wAddressValidatorData8.info(
                node = FakeData8DataSource.dummyLeafNode
            )

        assert(infoResult is Either.Right)
        infoResult as Either.Right
        with(infoResult.b as W3WStreetAddressData8) {
            assertEquals("WESTWAY COMMUNITY TRANSPORT", rawAddress?.organisation)
            assertEquals("W10 5YG", rawAddress?.postcode)
            assertEquals("LONDON", rawAddress?.locality)
            assertEquals("KENSINGTON AND CHELSEA", rawAddress?.administrativeCounty)
            assertEquals("", rawAddress?.postalCounty)
            assertEquals("240 Acklam Road", address?.lines?.get(1))
            assertEquals(7, address?.lines?.size)
            assertEquals("index.home.raft", rawAddress?.additionalData?.get(0)?.value)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test unhappy info path`() = runTest {
        `mock Data8APIService unsuccessful retrieve because of invalid API Key`()

        val infoResult: Either<W3WAddressValidatorError, W3WStreetAddress> =
            w3wAddressValidatorData8.info(
                node = FakeData8DataSource.dummyLeafNode
            )

        assert(infoResult is Either.Left)
        infoResult as Either.Left
        assertEquals(
            "API Key not recognised",
            infoResult.a.message
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService unsuccessful retrieve because of invalid API Key`() =
        runTest {
            // mock retrieve result
            `when`(
                data8APIService.retrieve(
                    key = MockitoHelper.anyObject(),
                    requestBody = MockitoHelper.anyObject()
                )
            ).thenReturn(FakeData8DataSource.unSuccessfulRetrieveResponse)
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService successful retrieve`() = runTest {
        // mock retrieve result
        `when`(
            data8APIService.retrieve(
                key = MockitoHelper.anyObject(),
                requestBody = MockitoHelper.anyObject()
            )
        ).thenReturn(FakeData8DataSource.successfulRetrieveResponse)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService second level successful drillDown`() = runTest {
        `when`(
            data8APIService.drillDown(
                key = MockitoHelper.anyObject(),
                requestBody = MockitoHelper.anyObject()
            )
        ).thenReturn(
            FakeData8DataSource.secondLevelSuccessfulDrillDownResponse
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService unsuccessful drillDown caused by invalid api key`() =
        runTest {
            `when`(
                data8APIService.drillDown(
                    key = MockitoHelper.anyObject(),
                    requestBody = MockitoHelper.anyObject()
                )
            ).thenReturn(
                FakeData8DataSource.unSuccessfulDrillDownResponseCauseByInvalidKey
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService root level successful drillDown`() = runTest {
        `when`(
            data8APIService.drillDown(
                key = MockitoHelper.anyObject(),
                requestBody = MockitoHelper.anyObject()
            )
        ).thenReturn(
            FakeData8DataSource.rootLevelSuccessfulDrillDownResponse
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService unsuccessful search caused by invalid API Key`() = runTest {
        `when`(
            data8APIService.search(
                key = MockitoHelper.anyObject(),
                requestBody = MockitoHelper.anyObject()
            )
        ).thenReturn(
            FakeData8DataSource.unSuccessfulSearchByInvalidKeyResponse
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun `mock Data8APIService successful search`() = runTest {
        // mock search result
        `when`(
            data8APIService.search(
                key = MockitoHelper.anyObject(),
                requestBody = MockitoHelper.anyObject()
            )
        ).thenReturn(
            FakeData8DataSource.successfulSearchResponse
        )
    }

}