package com.what3words.components.validator.core.addresslookupservice.loqate.utils

import com.what3words.javawrapper.What3WordsV3
import com.what3words.javawrapper.request.ConvertToCoordinatesRequest
import com.what3words.javawrapper.response.ConvertToCoordinates
import com.what3words.javawrapper.response.Coordinates
import org.mockito.Mockito


internal object LoqateMockUtils {
    internal fun mockConvertToCoordinatesCall(
        what3WordsV3: What3WordsV3,
        threeWordAddress: String
    ) : Coordinates{
        /** mock the default coordinates that will be returned by [What3WordsV3] java wrapper when the [What3WordsV3.convertToCoordinates] method is invoked **/
        val coordinates: Coordinates = Mockito.mock(Coordinates::class.java)
        Mockito.`when`(coordinates.lat).thenReturn(51.520847)
        Mockito.`when`(coordinates.lng).thenReturn(-0.195521)

        val convertToCoordinates: ConvertToCoordinates =
            Mockito.mock(ConvertToCoordinates::class.java)
        Mockito.`when`(convertToCoordinates.coordinates).thenReturn(coordinates)

        val convertToCoordinatesRequest: ConvertToCoordinatesRequest.Builder =
            Mockito.mock(ConvertToCoordinatesRequest.Builder::class.java)
        Mockito.`when`(convertToCoordinatesRequest.execute()).thenReturn(convertToCoordinates)
        Mockito.`when`(
            what3WordsV3.convertToCoordinates(threeWordAddress)
        ).thenReturn(
            convertToCoordinatesRequest
        )
        return coordinates
    }
}