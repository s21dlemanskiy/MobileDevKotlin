package com.example.task37

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import ru.auto.mockwebserver.dsl.WebServerRule

class  SongsManager<T: ComponentActivity>(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<T>, T>,
    private val webServerRule: WebServerRule
) {

    fun checkCardExists(id: Int) {
        runTest {
            composeTestRule.waitForIdle()
            delay(5000)
//        composeTestRule.waitUntil (timeoutMillis = 5000) {
//            composeTestRule.onNodeWithTag("Tag:Card $id", useUnmergedTree = true).isDisplayed()
//        }
            Log.i("Tests", "Check card with id $id exists")
            composeTestRule.onNodeWithTag("Tag:Card $id", useUnmergedTree = true).assertExists()
        }
    }

    fun checkCardContentExists(id: Int){
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Tag:Card del button $id").assertExists()
        composeTestRule.onNodeWithTag("Tag:Card text $id").assertExists()
        composeTestRule.onNodeWithTag("Tag:Card text $id").assertExists()
        composeTestRule.onNodeWithTag("Tag:Card author $id").assertExists()
    }
}