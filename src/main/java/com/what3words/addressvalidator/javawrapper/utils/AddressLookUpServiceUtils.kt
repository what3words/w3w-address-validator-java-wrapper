package com.what3words.addressvalidator.javawrapper.utils

import com.what3words.addressvalidator.javawrapper.What3WordsAddressValidator
import com.what3words.addressvalidator.javawrapper.addresslookupservice.AddressLookUpService
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.What3WordsAddressValidatorLoqate
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.LoqateApi
import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.What3WordsAddressValidatorSwiftComplete
import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.SwiftCompleteApi
import com.what3words.javawrapper.What3WordsV3
import kotlinx.coroutines.Dispatchers

internal object AddressLookUpServiceUtils {

    fun AddressLookUpService.getAddressValidator(): What3WordsAddressValidator {
        return when (this) {
            is AddressLookUpService.SwiftComplete -> {
                What3WordsAddressValidatorSwiftComplete(
                    swiftCompleteApiKey = key,
                    swiftCompleteAPIService = SwiftCompleteApi.swiftCompleteApiService,
                    coroutineDispatcher = Dispatchers.IO
                )
            }
            is AddressLookUpService.Data8 -> {
                throw RuntimeException("No location provider Data8")
            }
            is AddressLookUpService.Loqate -> {
                What3WordsAddressValidatorLoqate(
                    what3WordsV3 = What3WordsV3(w3wApiKey),
                    loqateApiKey = key,
                    loqateAPIService = LoqateApi.loqateApiService,
                    coroutineDispatcher = Dispatchers.IO
                )
            }
        }
    }
}