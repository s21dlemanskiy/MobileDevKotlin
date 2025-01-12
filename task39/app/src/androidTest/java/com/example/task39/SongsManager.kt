package com.example.task39

import android.content.Context
import android.util.Log
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import com.example.task39.model.models.RawSong
import ru.auto.mockwebserver.dsl.WebServerRule

class  SongsManager(
    private val composeTestRule: ComposeContentTestRule,
    private val webServerRule: WebServerRule
) {

    fun checkCardExists(id: Int) {
            composeTestRule.waitForIdle()
            Log.i("My Test", "Check card with id $id exists")
            composeTestRule.onNodeWithTag("Tag:Card $id", useUnmergedTree = true).assertExists()
    }

    fun checkCardContentExists(id: Int){
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("Tag:Card del button $id").assertExists()
        composeTestRule.onNodeWithTag("Tag:Card text $id").assertExists()
        composeTestRule.onNodeWithTag("Tag:Card title $id").assertExists()
        composeTestRule.onNodeWithTag("Tag:Card author $id").assertExists()
    }

    fun getCartContent(id: Int, authorId: Int = 0): RawSong {
        val text = composeTestRule.onNodeWithTag("Tag:Card text $id").assertExists()
            .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
        val title = composeTestRule.onNodeWithTag("Tag:Card title $id").assertExists()
            .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
        var author = composeTestRule.onNodeWithTag("Tag:Card author $id").assertExists()
            .fetchSemanticsNode().config[SemanticsProperties.Text].first().text
        val context = ApplicationProvider.getApplicationContext<Context>()
        val authorPreString = context.getString(R.string.autor_pre_string)
        author = author.drop(authorPreString.length)
        return RawSong(id = id, text = text, title = title, author_id = authorId)

    }
}