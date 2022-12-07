package loqate.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.FindItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.GeoLocationItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.LoqateResponseDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.RetrieveItemDTO
import utils.FileUtils

object FakeLoqateDataSource {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()).build()

    internal val geoLocationItems: List<GeoLocationItemDTO> =
        listOf(
            GeoLocationItemDTO(
                id = "RG|040060165070160015230232122223248253006121052209253094060246012117007083183071234202189102251129",
                type = "Address",
                text = "Flat 56",
                description = "London, W2 5HE",
                latitude = "51.521312713623",
                longitude = "-0.194834396243095"
            ),
            GeoLocationItemDTO(
                id = "RG|119060042041088019077195066016239059009162198221201161101041015090085034064001069111027158029131",
                type = "Address",
                text = "Flat 61",
                description = "London, W2 5HF",
                latitude = "51.521312713623",
                longitude = "-0.194834396243095"
            ),
            GeoLocationItemDTO(
                id = "RG|142036091005148124239078199192123118237006122115125045040063040252124046081166014140158040025095",
                type = "Address",
                text = "Flat 4",
                description = "London, W2 5ER",
                latitude = "51.521125793457",
                longitude = "-0.195634603500366"
            )
        )

    internal val findItems: List<FindItemDTO> = listOf(
        FindItemDTO(
            id = "GB|RM|A|25778756",
            type = "Address",
            text = "Flat 56, Oversley House, Alfred Road",
            highlight = "0-7;0-6,8-10,11-14",
            description = "London, W2 5HE"
        ), FindItemDTO(
            id = "GB|RM|A|25778791",
            type = "Address",
            text = "Flat 61, Oversley House, Alfred Road",
            highlight = "0-7;0-6,8-10,11-14",
            description = "London, W2 5HF"
        ), FindItemDTO(
            id = "GB|RM|A|25778353",
            type = "Address",
            text = "Flat 4, Radway House, Alfred Road",
            highlight = "0-6;0-6,8-10,11-14",
            description = "London, W2 5ER"
        )
    )

    internal fun getLoqateRetrieveItemResponse(): LoqateResponseDTO<RetrieveItemDTO>? {
        val loqateResponseDTORetrieveItemType = Types.newParameterizedType(
            LoqateResponseDTO::class.java, RetrieveItemDTO::class.java
        )
        return FileUtils.readJSON(
            type = loqateResponseDTORetrieveItemType,
            fileName = "loqateRetrieve.json"
        )
    }


}