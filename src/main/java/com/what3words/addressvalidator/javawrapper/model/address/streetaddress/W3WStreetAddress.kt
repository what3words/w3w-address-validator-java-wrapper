package com.what3words.addressvalidator.javawrapper.model.address.streetaddress

import java.io.Serializable

/**
 * @property words what3words address for the address
 * @property description a human readable form of the address.
 * This property has the complete form of the address, from the most specific part (e.g building number, street number) to the vague parts like city name or post postcode
 * @property primary Building name, flat number or street number for the address
 * @property secondary secondary address can include street name, city name, postcode etc, basically any other details that can provide more information on the primary address
 * ***/
sealed class W3WStreetAddress(
    val words: String?,
    val description: String?,
    val primary: String?,
    val secondary: String?
) : Serializable
