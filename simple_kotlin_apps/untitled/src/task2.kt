open class Task2 {
    fun main(){
        print("year:")
        var year = readlnOrNull()?.toLongOrNull()
        print("month:")
        var month = readlnOrNull()?.toLongOrNull()
        print("day:")
        var day = readlnOrNull()?.toLongOrNull()
        if (year == null || month == null || day == null) {
            throw RuntimeException("value cant be null")
        }
        if (day > maxDayOfMonth(month) || day < 1) {
            throw RuntimeException("day is not right ")
        }
        if (month > 12 || month < 1) {
            throw RuntimeException("month is not right ")
        }


        day += 1
        if (day > maxDayOfMonth(month)) {
            day -= maxDayOfMonth(month)
            month += 1
        }
        if (month > 12) {
            month -= 12
            year += 1
        }
        println(dateToString(day, month, year))

    }

    fun maxDayOfMonth(month: Long): Long {
        return when (month) {
            1L -> 31
            2L -> 28
            3L -> 31
            4L -> 30
            5L -> 31
            6L -> 30
            7L -> 31
            8L -> 31
            9L -> 30
            10L -> 31
            11L -> 30
            12L -> 31
            else -> throw RuntimeException("month not right")
        }
    }

    fun dateToString(day: Long, month: Long, year: Long): String {
        return "$day ${monthToString(month)} $year"
    }

    fun monthToString(month: Long): String {
        return when (month) {
            1L -> "Январь"
            2L -> "Февраль"
            3L -> "Март"
            4L -> "Апрель"
            5L -> "Май"
            6L -> "Июнь"
            7L -> "Июль"
            8L -> "Август"
            9L -> "Сентябрь"
            10L -> "Октябрь"
            11L -> "Ноябрь"
            12L -> "Декабрь"
            else -> throw RuntimeException("month not right and cant be map to string")
        }
    }
}