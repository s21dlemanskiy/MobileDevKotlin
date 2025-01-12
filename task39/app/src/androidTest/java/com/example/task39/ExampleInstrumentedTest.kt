package com.example.task39

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.FileNotFoundException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.task37", appContext.packageName)
    }

    @Test
    fun a(){
        val context = InstrumentationRegistry.getInstrumentation().context

        // Выведем список всех доступных assets
        context.assets.list("")?.forEach {
            Log.d("AssetTest", "Available asset: $it")
        }

        // Попробуем разные варианты пути
        val variants = listOf(
            "songsWebMock.json",
            "/songsWebMock.json",
            "webmocks/songsWebMock.json",
            "/webmocks/songsWebMock.json"
        )

        variants.forEach { path ->
            try {
                val content = context.assets.open(path).bufferedReader().use { it.readText() }
                Log.d("AssetTest", "Successfully read from: $path")
                Log.d("AssetTest", "Content: $content")
            } catch (e: FileNotFoundException) {
                Log.e("AssetTest", "Failed to read: $path", e)
            }
        }
    }
}