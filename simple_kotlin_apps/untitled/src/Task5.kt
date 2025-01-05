class Task5 {
    // сумма четных чисел
    // 12345 -> 6 тк 2 + 4
    fun main() {
        val number = readlnOrNull()?.toIntOrNull()
        if (number == null) {
            println("Invalid input")
            return
        }
        println("Сумма чисел ${sumDigits(number) { it % 2 == 0 }}")
    }


    fun sumDigits(number: Int, filter: (Int) -> Boolean): Int = number
        .toString().toCharArray()
        .map { it.digitToInt() }.filter(filter).sum()
}