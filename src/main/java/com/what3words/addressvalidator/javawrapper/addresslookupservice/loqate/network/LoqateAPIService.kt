package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network

import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.FindItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.GeoLocationItemDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.LoqateResponseDTO
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.RetrieveItemDTO
import retrofit2.http.POST
import retrofit2.http.Query

private const val loqateNumResults = 100
private const val loqateRadius = 200
private const val geolocationPath = "Capture/Interactive/GeoLocation/v1/json3.ws"
private const val findPath = "Capture/Interactive/Find/v1.1/json3.ws"
private const val retrievePath = "Capture/Interactive/Retrieve/v1.2/json3.ws"
private const val supportedCountries = "GB"

internal interface LoqateAPIService {

    @POST(geolocationPath)
    suspend fun geoLocate(
        @Query("Key") key: String,
        @Query("Latitude") lat: Double,
        @Query("Longitude") long: Double,
        @Query("Items") items: Int = loqateNumResults,
        @Query("Radius") radius: Int = loqateRadius
    ): LoqateResponseDTO<GeoLocationItemDTO>

    @POST(findPath)
    suspend fun find(
        @Query("Key") key: String,
        @Query("Text") text: String,
        @Query("Countries") countries: String = supportedCountries,
        @Query("Limit") limit: Int = 1,
        @Query("Language") language: String = "en"
    ): LoqateResponseDTO<FindItemDTO>

    @POST(retrievePath)
    suspend fun retrieve(
        @Query("Key") key: String,
        @Query("Id") id: String
    ): LoqateResponseDTO<RetrieveItemDTO>

}