package com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network

import com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.dto.SwiftCompleteAddressDTO
import retrofit2.http.GET
import retrofit2.http.Query


private const val RESULT_ORDERING = "location_biasing"
private const val MAX_NUMBER_OF_RESULTS = 100
private const val MAX_DISTANCE_TO_SEARCH = 3000
private const val POPULATE_LINE_COUNT = 6

internal interface SwiftCompleteAPIService {

    @GET("places/")
    suspend fun search(
        @Query("key") key: String,
        @Query("biasTowards") threeWordAddress: String,
        @Query("groupBy") groupBy: String = "road, emptyRoad",
        @Query("populateIndex") populateIndex: Int? = null,
        @Query("populateLineCount") populateLineCount: Int = POPULATE_LINE_COUNT
        /*@Query("searchFor") searchFor: String = "what3words, address",*/
    ): List<SwiftCompleteAddressDTO>


    @GET("places/")
    suspend fun list(
        @Query("key") key: String,
        @Query("container") container: String,
        @Query("biasTowards") threeWordAddress: String,
        @Query("maxResults") maxResult: Int = MAX_NUMBER_OF_RESULTS,
        @Query("maxDistance") maxDistance: Int = MAX_DISTANCE_TO_SEARCH,
        @Query("resultOrdering") resultOrdering: String = RESULT_ORDERING,
        @Query("populateIndex") populateIndex: Int? = null,
        @Query("populateLineCount") populateLineCount: Int = POPULATE_LINE_COUNT
    ): List<SwiftCompleteAddressDTO>

}
