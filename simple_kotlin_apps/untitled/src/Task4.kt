import java.lang.StringBuilder

class Task4 {
    //. Найдите первый символ в первом максимально длинном слове с нечетным числом сим-
    // волов в строке (в строке указываются только слова, разделенные одним или несколькими пробелами).
    // qw wer erty rtyui -> r
    fun main() {
        var input = readlnOrNull()
        while (input == null) {
            println("input is null")
            input = readlnOrNull()
        }
//        val words = input.split(Regex("\\s+"))
//            .filter { it.length % 2 != 0 }
//        assert(words.isNotEmpty())
//        val word = words.reduce{ acc, word -> if(word.length > acc.length) word else acc }
//        assert(word.length > 0)
//        println(word.first())

        val (start, end) = findLongestWord(input)
        if (start == null) {
            println("word not found")
            return
        }
        println(input[start])
    }

    fun findLongestWord(wordsSeparetedBySpaces: String): Pair<Int?, Int?> {
        var start: Int? = null
        var end: Int? = null
        var currStart = 0
        var currEnd = 0
        for (i in wordsSeparetedBySpaces.indices) {
            if (!wordsSeparetedBySpaces[i].isWhitespace()) {
                currEnd = i + 1
            } else {
                if ((currEnd - currStart) % 2 != 0 && (start == null || end == null || currEnd - currStart > end - start)) {
                    end = currEnd
                    start = currStart
                }
                currStart = i + 1

            }
        }
        if ((currEnd - currStart) % 2 != 0 && (start == null || end == null || currEnd - currStart > end - start)) {
            end = currEnd
            start = currStart
        }
        return start to end
    }
}