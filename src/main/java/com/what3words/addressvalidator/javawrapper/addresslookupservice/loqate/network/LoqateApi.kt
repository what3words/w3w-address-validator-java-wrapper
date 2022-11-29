package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.addressy.com/"

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()).build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

internal object LoqateApi {
    val loqateApiService: LoqateAPIService by lazy {
        retrofit.create(LoqateAPIService::class.java)
    }
}