package com.what3words.addressvalidator.javawrapper.addresslookupservice


sealed class AddressLookUpService(val key: String) {

    data class SwiftComplete(private val swiftCompleteApiKey: String) :
        AddressLookUpService(key = swiftCompleteApiKey)


    data class Loqate(val w3wApiKey: String, private val loqateApiKey: String) :
        AddressLookUpService(key = loqateApiKey)


    data class Data8(private val data8ApiKey: String) : AddressLookUpService(key = data8ApiKey)
}