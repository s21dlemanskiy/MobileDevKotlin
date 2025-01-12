package com.example.task38

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.task38.model.RetrofitInstance
import com.example.task38.utils.RawSongsList
import com.example.task38.utils.loadJsonAsObject
import com.example.task38.utils.mockPath
import com.example.task38.view.SongListScreen
import com.example.task38.viewmodel.SongViewModel
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
    lateinit var songViewModel: SongViewModel

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
    val composeTestRule =  createComposeRule()


    @Before
    fun setUp() {
        composeTestRule.setContent {
            songViewModel = viewModel()
            SongListScreen(songViewModel = songViewModel, padding = PaddingValues.Absolute())
        }
        RetrofitInstance.updateBaseUrl(webServerRule.webServer.url("/").toUrl().toString())
    }

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
        composeTestRule.waitUntil {
            songViewModel.songs.value.isNotEmpty()
        }
        composeTestRule.onRoot().printToLog("UITree")
    }

    @Test
    fun getSongsList() {
        composeTestRule.waitUntil {
            songViewModel.songs.value.isNotEmpty()
        }
        val rawSongs: RawSongsList = loadJsonAsObject<RawSongsList>(songsAssetPath)
        val manager = SongsManager(composeTestRule, webServerRule)
        Log.i("MyTest", rawSongs.songs.toString())
        rawSongs.songs.forEach {
            Log.i("MyTest", rawSongs.songs.toString())
            manager.checkCardExists(it.id)
        }
    }

    @Test
    fun contentCompare() {
        composeTestRule.waitUntil {
            songViewModel.songs.value.isNotEmpty()
        }
        val rawSongs: RawSongsList = loadJsonAsObject<RawSongsList>(songsAssetPath)
        val manager = SongsManager(composeTestRule, webServerRule)
        Log.i("MyTest", rawSongs.songs.toString())
        rawSongs.songs.forEach {
            assertEquals(it, manager.getCartContent(it.id, it.author_id))
        }
    }
}