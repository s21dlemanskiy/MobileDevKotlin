import java.math.BigInteger


data class Fraction(val numerator: BigInteger, val denominator: BigInteger) {
    init {
        require(denominator != 0.toBigInteger()) { "Знаменатель не может быть равен нулю." }
    }

    // Метод для умножения дробей
    operator fun times(other: Fraction): Fraction {
        val newNumerator = this.numerator * other.numerator
        val newDenominator = this.denominator * other.denominator
        return Fraction(newNumerator, newDenominator).simplify()
    }

    // Метод для деления дробей
    operator fun div(other: Fraction): Fraction {
        require(other.numerator != BigInteger("0")) { "Деление на ноль." }
        val newNumerator = this.numerator * other.denominator
        val newDenominator = this.denominator * other.numerator
        return Fraction(newNumerator, newDenominator).simplify()
    }

    // Метод для упрощения дроби
    fun simplify(): Fraction {
        val gcd = gcd(numerator, denominator)
        return Fraction(numerator / gcd, denominator / gcd)
    }

    override fun toString(): String {
        return "$numerator/$denominator"
    }

companion object {
    // Метод для нахождения НОД
    @JvmStatic
    fun gcd(a: BigInteger, b: BigInteger): BigInteger {
        return if (b == 0.toBigInteger()) a.abs() else gcd(b, a % b)
    }
}


}