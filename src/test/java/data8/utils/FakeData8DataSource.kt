package data8.utils

import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response.Data8RetrieveResponse
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response.Data8SearchOrDrillResponse
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorLeafNode
import com.what3words.addressvalidator.javawrapper.model.address.W3WAddressValidatorParentNode
import utils.FileUtils

internal object FakeData8DataSource {


    val dummyParentNode: W3WAddressValidatorParentNode = W3WAddressValidatorParentNode(
        primaryAddress = "",
        secondaryAddress = "",
        threeWordAddress = "",
        container = "",
        parent = null
    )

    val dummyLeafNode: W3WAddressValidatorLeafNode = W3WAddressValidatorLeafNode(
        primaryAddress = "",
        secondaryAddress = "",
        threeWordAddress = "",
        parent = null,
        id = ""
    )

    val successfulSearchResponse: Data8SearchOrDrillResponse?
        get() = FileUtils.readJSON(
            type = Data8SearchOrDrillResponse::class.java,
            fileName = "data8/data8SuccessfulSearch.json"
        )

    val unSuccessfulSearchByInvalidKeyResponse: Data8SearchOrDrillResponse?
        get() = FileUtils.readJSON(
            type = Data8SearchOrDrillResponse::class.java,
            fileName = "data8/data8UnsuccessfulSearchByInvalidAPIKey.json"
        )

    val rootLevelSuccessfulDrillDownResponse: Data8SearchOrDrillResponse?
        get() = FileUtils.readJSON(
            type = Data8SearchOrDrillResponse::class.java,
            fileName = "data8/data8RootLevelSuccessfulDrillDown.json"
        )

    val secondLevelSuccessfulDrillDownResponse: Data8SearchOrDrillResponse?
        get() = FileUtils.readJSON(
            type = Data8SearchOrDrillResponse::class.java,
            fileName = "data8/data8SecondLevelSuccessfulDrillDown.json"
        )

    val unSuccessfulDrillDownResponseCauseByInvalidKey: Data8SearchOrDrillResponse?
        get() = FileUtils.readJSON(
            type = Data8SearchOrDrillResponse::class.java,
            fileName = "data8/data8UnsuccessfulDrillDownByInvalidAPIKey.json"
        )

    val successfulRetrieveResponse: Data8RetrieveResponse?
        get() = FileUtils.readJSON(
            type = Data8RetrieveResponse::class.java,
            fileName = "data8/data8SuccessfulRetrieve.json"
        )

    val unSuccessfulRetrieveResponse: Data8RetrieveResponse?
        get() = FileUtils.readJSON(
            type = Data8RetrieveResponse::class.java,
            fileName = "data8/data8UnsuccessfulRetrieveByInvalidaAPIKey.json"
        )

}