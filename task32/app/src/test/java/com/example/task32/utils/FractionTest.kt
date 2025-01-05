package com.example.task32.utils

import Fraction
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigInteger
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource

// junit5 give me some shit so use junut4 aka org.junit instead of junut5 aka org.junit.jupiter
//import org.junit.jupiter.api.Test

//import org.junit.jupiter.api.Assertions.*


class FractionTest {

    @DisplayName("Multiply Test Cases 😎")
    @ParameterizedTest(name = "{0} * {1} = {2}")
    @CsvSource(
        "1/5, 2/3, 2/15",
        "0/15, 3/7, 0"
        )
    fun times(
        @ConvertWith(FractionConverter::class) arg1: Fraction,
        @ConvertWith(FractionConverter::class) arg2: Fraction,
        @ConvertWith(FractionConverter::class) realResult: Fraction
    ) {
        assertEquals(realResult.simplify(), arg1 * arg2)
    }

    @DisplayName("Divide Test Cases 😎")
    @ParameterizedTest(name = "({0}) / ({1}) = {2}")
    @CsvSource(
        "1/5, 2/3, 3/10",
        "0, 3/7, 0"
    )
    fun div(
        @ConvertWith(FractionConverter::class) arg1: Fraction,
        @ConvertWith(FractionConverter::class) arg2: Fraction,
        @ConvertWith(FractionConverter::class) realResult: Fraction
    ) {
        assertEquals(realResult.simplify(), arg1 / arg2)
    }

    @Test
    fun simplify() {
        val a = Fraction(10.toBigInteger(), 5.toBigInteger())
        assertEquals(Fraction(2.toBigInteger(), 1.toBigInteger()), a.simplify())
    }

    @Test
    fun simplifyZero() {
        val a = Fraction(0.toBigInteger(), 5.toBigInteger())
        // yes there is 1. it is due gcd algorithm
        assertEquals( Fraction(0.toBigInteger(), 1.toBigInteger()), a.simplify())
    }

    @ParameterizedTest
    @MethodSource("gcdParams")
    fun gcd(a:BigInteger, b: BigInteger, realResult: BigInteger) {
            val result = Fraction.gcd(a,b)
//            println("$result, $realResult")
            assertEquals(realResult, result)

    }
//
    @Test
    fun getNumerator() {
        val numerator = 10.toBigInteger()
        val frac = Fraction(numerator, 1.toBigInteger())
        assertEquals(numerator, frac.numerator)
    }

    @Test
    fun getDenominator() {
        val denominator = 10.toBigInteger()
        val frac = Fraction(1.toBigInteger(), denominator)
        assertEquals(denominator, frac.denominator)
    }

    @Test
    fun zeroDivision1() {
        assertThrowsExactly(IllegalArgumentException::class.java) {
            Fraction(10.toBigInteger(), 0.toBigInteger())
        }
//        try {
//            Fraction(10.toBigInteger(), 0.toBigInteger()) }
//        catch (_: IllegalArgumentException){}
    }
    companion object {
        @JvmStatic
        fun gcdParams() = listOf(
            Arguments.of(1.toBigInteger(), 1.toBigInteger(), 1.toBigInteger()),
            Arguments.of(5.toBigInteger(), 4.toBigInteger(), 1.toBigInteger()),
            Arguments.of(100.toBigInteger(), 101.toBigInteger(), 1.toBigInteger()),
            Arguments.of(100.toBigInteger(), 8.toBigInteger(), 4.toBigInteger()),
            // negative numbers
            Arguments.of(-10.toBigInteger(), 20.toBigInteger(), 10.toBigInteger()),
            Arguments.of(10.toBigInteger(), -20.toBigInteger(), 10.toBigInteger()),
            Arguments.of(-10.toBigInteger(), -20.toBigInteger(), 10.toBigInteger()),
            // bad case
            Arguments.of(10.toBigInteger(), 0.toBigInteger(), 10.toBigInteger()),
            Arguments.of(0.toBigInteger(), 10.toBigInteger(), 10.toBigInteger()),
        )
    }
}