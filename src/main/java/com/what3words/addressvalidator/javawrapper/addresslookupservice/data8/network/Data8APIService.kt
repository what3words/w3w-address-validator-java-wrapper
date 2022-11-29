package com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network

import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.request.Data8DrillDownAndRetrieveRequestBody
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.request.Data8SearchRequestBody
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response.Data8RetrieveResponse
import com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.response.Data8SearchOrDrillResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


private const val searchEndPoint = "PredictiveAddress/Search.json"
private const val drillDownEndPoint = "PredictiveAddress/DrillDown.json"
private const val retrieveEndPoint = "PredictiveAddress/Retrieve.json"

internal interface Data8APIService {

    @POST(searchEndPoint)
    suspend fun search(
        @Query("key") key: String,
        @Body requestBody: Data8SearchRequestBody
    ): Data8SearchOrDrillResponse


    @POST(drillDownEndPoint)
    suspend fun drillDown(
        @Query("key") key: String,
        @Body requestBody: Data8DrillDownAndRetrieveRequestBody
    ): Data8SearchOrDrillResponse

    @POST(retrieveEndPoint)
    suspend fun retrieve(
        @Query("key") key: String,
        @Body requestBody: Data8DrillDownAndRetrieveRequestBody
    ): Data8RetrieveResponse
}