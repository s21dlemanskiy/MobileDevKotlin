package com.example.task37

import android.util.Log
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.task37.model.RetrofitInstance
import com.example.task37.model.models.RawSong
import com.example.task37.utils.RawSongsList
import com.example.task37.utils.loadJsonAsObject
import com.example.task37.utils.mockPath
import com.example.task37.view.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Request

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import ru.auto.mockwebserver.dsl.WebServerRule
import java.net.InetAddress

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SimpleIntegratedTest {
    val songsAssetPath = "songsWebMock.json"
    val authorsAssetPath = "authorsWebMock.json"

    val testAddress = InetAddress.getByName("localhost")
    val testWebPort = 8181
    @get:Rule(order = 0)
    val webServerRule = WebServerRule(
        inetAddress = testAddress,
        webPort = testWebPort
    ) {
        mockPath(path="/songs", assetPath = songsAssetPath)
        mockPath(path="/authors", assetPath = authorsAssetPath)
    }

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp() {
//        webServerRule.startWebServer()
        RetrofitInstance.updateBaseUrl(webServerRule.webServer.url("/").toUrl().toString())
    }
//
//    @After
//    fun tearDown() {
//        webServerRule.shutdownWebServer()
//    }

//    private lateinit var mockWebServer: MockWebServer
//    @Before
//    fun setup() {
//        mockWebServer = MockWebServer()
//        mockWebServer.start(InetAddress.getByName("localhost"), 8181)
//    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.task37", appContext.packageName)
    }

    @Test
    fun testServerIsRunning() {
        // Проверка, что сервер отвечает и webServerRule не говнит
        val response = OkHttpClient().newCall(
            Request.Builder()
                .url(webServerRule.webServer.url("/songs"))
                .build()
        ).execute()

        assertTrue(response.isSuccessful)
    }

    @Test
    fun printTree() {
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().printToLog("UITree")
    }

    @Test
    fun getSongsList() {
        val rawSongs: RawSongsList = loadJsonAsObject<RawSongsList>(songsAssetPath)
        val manager = SongsManager(composeTestRule, webServerRule)
        Log.i("MyTest", "ssss")
        Log.i("MyTest", rawSongs.songs.toString())
        Log.i("MyTest", rawSongs.songs.map { it.id }.toString())
        for (id in rawSongs.songs.map { it.id }) {
//            manager.checkCardExists(id)
        }
    }
//


}