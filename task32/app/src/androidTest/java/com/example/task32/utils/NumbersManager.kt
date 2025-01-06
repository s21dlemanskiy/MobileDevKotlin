package com.example.task32.utils

import Fraction
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.task32.R
import java.math.BigInteger

class  NumbersManager<T: ComponentActivity>(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<T>, T>
) {

    fun setArg1(arg: Fraction) {
        composeTestRule.onNodeWithTag("Tag:1-st fraction numerator").performTextInput(arg.numerator.toString())
        composeTestRule.onNodeWithTag("Tag:1-st fraction denumerator").performTextInput(arg.denominator.toString())
    }

    fun setArg2(arg: Fraction) {
        composeTestRule.onNodeWithTag("Tag:2-st fraction numerator").performTextInput(arg.numerator.toString())
        composeTestRule.onNodeWithTag("Tag:2-st fraction denumerator").performTextInput(arg.denominator.toString())
    }

    fun processAndGetResult(): Fraction {
        process()
        val numerator = composeTestRule.onNodeWithTag("Tag:result fraction numerator")
            .fetchSemanticsNode().config[SemanticsProperties.Text].firstOrNull()?.text
        val denumerator = composeTestRule.onNodeWithTag("Tag:result fraction denumerator")
            .fetchSemanticsNode().config[SemanticsProperties.Text].firstOrNull()?.text
//        return Fraction(
//            BigInteger(numerator ?: "1"),
//            BigInteger(denumerator ?: "1")
//        )
        return Fraction(
            BigInteger(numerator!!),
            BigInteger(denumerator!!)
        )
    }

    fun process() {
        composeTestRule.onNodeWithTag("Tag:processOperation").performClick()
        // waiting for UI changes
        composeTestRule.waitForIdle()
    }

    fun setSign(sign: Sign) {
        val targetSign = sign.getSign()
        var i = 0
        while (i++ < Sign.entries.size) {
            val currSignButtonText = composeTestRule.onNodeWithTag("Tag:signButton")
                .fetchSemanticsNode().config[SemanticsProperties.Text]
                .firstOrNull()?.text ?: ""
            if (currSignButtonText == targetSign) {
                return
            }
            composeTestRule.onNodeWithTag("Tag:signButton").performClick()
            // waiting for UI changes
            composeTestRule.waitForIdle()
        }
        throw RuntimeException("Can't set sign '$targetSign'")
    }
    companion object {
        enum class Sign(
            val resourceString: Int
        ) {
            MULTIPLY(resourceString = R.string.prod),
            DIVISION(resourceString = R.string.devide)
        }
        fun Sign.getSign(): String {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            return context.getString(resourceString)
        }
    }
}