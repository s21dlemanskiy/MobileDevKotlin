class Task1 {
    fun main () {
        var inputVal = readlnOrNull()?.toIntOrNull();
        if (inputVal == null) {
            println("${inputVal} is not a digit")
            return
        }
        var minDigit: Int? = null
        while (inputVal != 0) {
            val digit = inputVal % 10
            if (minDigit == null || digit > minDigit) {
                minDigit = digit
            }
            inputVal /= 10
        }
        println("minDigit: ${minDigit}")
    }
}