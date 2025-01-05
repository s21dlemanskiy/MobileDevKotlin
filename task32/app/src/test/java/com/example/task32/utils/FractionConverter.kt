package com.example.task32.utils

import Fraction
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.converter.ArgumentConversionException
import org.junit.jupiter.params.converter.ArgumentConverter
import java.math.BigInteger

class FractionConverter: ArgumentConverter {
    override fun convert(source: Any?, context: ParameterContext?): Fraction {
        if (source is String) {
            val parts = source.split("/")
            if (parts.isNotEmpty()) {
                val numerator = parts[0].trim().toBigInteger()
                val denominator = parts.getOrNull(1)?.trim()?.toBigIntegerOrNull() ?: BigInteger.ONE
                return Fraction(numerator, denominator)
            }
        }
        throw ArgumentConversionException("Failed to convert $source to Fraction")
    }
}