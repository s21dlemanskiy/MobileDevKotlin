package com.example.task38.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Headers
import org.hamcrest.CoreMatchers
import ru.auto.mockwebserver.dsl.Routing
import ru.auto.mockwebserver.dsl.get
import ru.auto.mockwebserver.dsl.response

fun Routing.mockPath(path: String, assetPath: String) = get(
    description = "get feed, path:$path contentPath:$assetPath",
    request = {
        CoreMatchers.containsString(path)
    },
    response = response {
        setBody(testLoadResource(assetPath))
        setHeaders(Headers.headersOf("mimetype", "application/json"))
    }
)

inline fun <reified T> loadJsonAsObject(assetPath: String): T {
    val json = testLoadResource(assetPath)
    return JsonParser().parseJson(json)
}

class JsonParser {
    private val objectMapper = ObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    }

    fun <T> parseJson(json: String, clazz: Class<T>): T {
        return try {
            objectMapper.readValue(json, clazz)
        } catch (e: Exception) {
            throw JsonParseException("Failed to parse JSON: ${e.message}")
        }
    }
    fun <T> parseJsonToList(json: String): List<T> {
        return try {
            objectMapper.readValue(json,
            object : TypeReference<List<T>>() {})
        } catch (e: Exception) {
            throw JsonParseException("Failed to parse List JSON: ${e.message}")
        }
    }


    inline fun <reified T> parseJson(json: String): T {
        return parseJson(json, T::class.java)
    }
}

fun testLoadResource(resPath: String): String {
//    val context = ApplicationProvider.getApplicationContext<Context>()
//    val assetManager = context.assets
//
//    val inputStream = assetManager.open(resPath)
//    val content = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
//    assertNotNull(content)
//    return content

    val context = InstrumentationRegistry.getInstrumentation().context
    return context
        .assets
        .open(resPath)
        .bufferedReader()
        .use { it.readText() }
}