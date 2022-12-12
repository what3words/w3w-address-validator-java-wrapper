package utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {
    inline fun <reified T> createRetrofitTestInstance(baseUrl: String): T {
        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory()).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(T::class.java)
    }
}