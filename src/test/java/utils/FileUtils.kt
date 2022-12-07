package utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.jetbrains.annotations.NotNull
import java.lang.reflect.Type

object FileUtils {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()).build()

    fun <T> readJSON(@NotNull type: Type, fileName: String): T? {
        val responseBody =
            ClassLoader.getSystemResource(fileName).readText(charset = Charsets.UTF_8)
        return moshi.adapter<T>(type)
            .fromJson(responseBody)
    }
}