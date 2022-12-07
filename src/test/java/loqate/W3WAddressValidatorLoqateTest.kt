package loqate


import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.What3WordsAddressValidatorLoqate
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.LoqateAPIService
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.GeoLocationItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.LoqateResponseDTO
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorRootNode
import com.what3words.addressvalidator.javawrapper.model.address.streetaddress.W3WStreetAddressLoqate
import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError
import com.what3words.addressvalidator.javawrapper.utils.Either
import loqate.utils.FakeLoqateDataSource
import com.what3words.components.validator.core.addresslookupservice.loqate.utils.LoqateMockUtils
import com.what3words.javawrapper.What3WordsV3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


internal class W3WAddressValidatorLoqateTest {
    private val threeWordAddress = "index.home.raft"
    private val what3WordsV3: What3WordsV3 = mock(What3WordsV3::class.java)
    private val loqateApiKey = ""
    private val loqateAPIService: LoqateAPIService = mock(LoqateAPIService::class.java)
    private val w3wAddressValidatorLoqate: What3WordsAddressValidatorLoqate = What3WordsAddressValidatorLoqate(
        what3WordsV3 = what3WordsV3,
        coroutineDispatcher = Dispatchers.IO,
        loqateApiKey = loqateApiKey,
        loqateAPIService = loqateAPIService
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() = runTest {
        /** mock the default coordinates that will be returned by [What3WordsV3] java wrapper when the [What3WordsV3.convertToCoordinates] method is invoked **/
        val coordinates =
            LoqateMockUtils.mockConvertToCoordinatesCall(what3WordsV3, threeWordAddress)
        /**
         * mock LoqateAPI responses for [LoqateAPIService.geoLocate] and [LoqateAPIService.find] methods
         * **/
        `when`(
            loqateAPIService.geoLocate(
                key = loqateApiKey,
                lat = coordinates.lat,
                long = coordinates.lng
            )
        ).thenReturn(
            LoqateResponseDTO(items = FakeLoqateDataSource.geoLocationItems)
        )

        FakeLoqateDataSource.geoLocationItems.forEachIndexed { index: Int, geoLocationItemDTO: GeoLocationItemDTO ->
            `when`(
                loqateAPIService.find(
                    key = loqateApiKey,
                    text = "${geoLocationItemDTO.text} ${geoLocationItemDTO.description}"
                )
            ).thenReturn(LoqateResponseDTO(items = listOf(FakeLoqateDataSource.findItems[index])))
        }

        /**mock mock LoqateAPI responses for [LoqateAPIService.retrieve]**/
        `when`(
            loqateAPIService.retrieve(
                key = loqateApiKey,
                id = "##"
            )
        ).thenReturn(
            FakeLoqateDataSource.getLoqateRetrieveItemResponse()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun search() = runTest {
        val searchResult: Either<W3WAddressValidatorError, W3WAddressValidatorRootNode> =
            w3wAddressValidatorLoqate.search(near = threeWordAddress)
        assert(searchResult.isRight)
        searchResult as Either.Right
        assertEquals(1, searchResult.b.children.size)
        assertEquals("Alfred Road", searchResult.b.children[0].primaryAddress)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun list() = runTest {
        val searchResult: Either.Right<W3WAddressValidatorRootNode> =
            w3wAddressValidatorLoqate.search(near = threeWordAddress) as Either.Right
        val listResult: Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>> =
            w3wAddressValidatorLoqate.list(node = searchResult.b.children[0] as W3WAddressValidatorParentNode)
        assert(listResult.isRight)
        listResult as Either.Right
        assertEquals(2, listResult.b.size)
        assert(listResult.b.find {
            it.primaryAddress == "Oversley House"
        } != null)
        assert(listResult.b.find {
            it.primaryAddress == "Flat 4"
        } != null)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun info() = runTest {
        val infoResult = w3wAddressValidatorLoqate.info(
            node = W3WAddressValidatorLeafNode(
                primaryAddress = "",
                secondaryAddress = "",
                threeWordAddress = "",
                id = "",
                parent = null
            )
        )
        assert(infoResult is Either.Right)
        infoResult as Either.Right
        val streetAddress = infoResult.b
        assert(streetAddress is W3WStreetAddressLoqate)
        streetAddress as W3WStreetAddressLoqate
        with(streetAddress) {
            assertEquals("Harford House", line2)
            assertEquals("35 Tavistock Crescent", line3)
            assertEquals("Flat 7", subBuilding)
            assertEquals("W11 1AY", postalCode)
            assertEquals("United Kingdom", countryName)
        }
    }
}