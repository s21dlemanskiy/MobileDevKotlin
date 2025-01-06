package com.example.task32

import Fraction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.task32.utils.NumbersManager
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SomeUITest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun normalProcessMultiply(){
        composeTestRule.onRoot(useUnmergedTree = false).printToLog("{tree useUnmergedTree=false}")
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("{tree useUnmergedTree=true}")
        val expectedResult = Fraction(2.toBigInteger(), 1.toBigInteger())
        val manager = NumbersManager(composeTestRule)
        manager.setSign(NumbersManager.Companion.Sign.MULTIPLY)
        manager.setArg1(Fraction(1.toBigInteger(), 1.toBigInteger()))
        manager.setArg2(Fraction(2.toBigInteger(), 1.toBigInteger()))
        val result = manager.processAndGetResult()
        println("real result:$result, expectedResult: $expectedResult")
        assert(result == expectedResult)
    }

    @Test
    fun normalProcessDivision(){
        val expectedResult = Fraction(1.toBigInteger(), 2.toBigInteger())
        val manager = NumbersManager(composeTestRule)
        manager.setSign(NumbersManager.Companion.Sign.DIVISION)
        manager.setArg1(Fraction(1.toBigInteger(), 1.toBigInteger()))
        manager.setArg2(Fraction(2.toBigInteger(), 1.toBigInteger()))
        val result = manager.processAndGetResult()
        println("real result:$result, expectedResult: $expectedResult")
        assert(result == expectedResult)
    }

    @Test
    fun zeroDivision(){
        val manager = NumbersManager(composeTestRule)
        manager.setSign(NumbersManager.Companion.Sign.DIVISION)
        manager.setArg1(Fraction(1.toBigInteger(), 1.toBigInteger()))
        manager.setArg2(Fraction(0.toBigInteger(), 1.toBigInteger()))
        manager.process()
        assert(composeTestRule.onNodeWithTag("Tag:alert message text").isDisplayed())
    }
}