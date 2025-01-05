class Task6 {
    // сумма цифр, стоящих на четных позициях в числе (если нумеровать цифры с конца): для числа 1234 ответ 4
//    1234 -> 6
    fun task1() {
        println(
            readlnOrNull().let {
                if (it == null) {
                    println("input error")
                    return
                }
                return@let it
            }.reversed()
                .mapIndexed { index, elem -> if(index % 2 == 0) elem.digitToIntOrNull() else null }
                .filterNotNull()
                .sum()
        )
    }

    // В строке указано несколько неотрицательных целых чисел, разделенных пробелами (по одному пробелу между числами).
    // В каком количестве чисел присутствуют все цифры от 0 до 9?
    // 0123456789 123456789 98765432101234567889 -> 2
    fun task3() {
        println(
            readlnOrNull().let {
                if (it == null) {
                    println("input error")
                    return
                }
                return@let it
            }.split(" ").count { word -> word.map { it.digitToInt() }.toSet().size == 10 })
    }

    //Найдите второй символ в первом максимально длинном слове с четным числом символов
    // в строке (в строке указываются только слова, разделенные одним или несколькими пробелами).
    // abcf fghр yyyyy -> b
    fun task4() {
        print(
            readlnOrNull().let {
                if (it == null) {
                    println("input error")
                    return
                }
                return@let it
            }.split(" ")
                .map{ it.trim() }
                .filter { it.length % 2 == 0 }
                .maxBy { it.length }
                .getOrNull(1)

        )
    }
    // 1111 10111 -> 1
    // 1111 11111 -> 0
    fun task64() {
        print(
            readlnOrNull().let {
                if (it == null) {
                    println("input error")
                    return
                }
                return@let it
            }.split(" ")
                .mapNotNull { number -> number.getOrNull(1)?.digitToIntOrNull() }
                .reduce { acc, value -> acc xor value }
        )

    }
    // двоечник, двоечник, 2 2 3 2 2;аркадий, жуткин, 3 4 5 7;арсений, мяуткин, 5 4 1 2;2вася, пупкин2, 3 4 5 6 3 4 5 6;3вася, пупкин3, 3 4 5 6 3 4 5 6
    // [аркадий жуткин, 2вася пупкин2, 3вася пупкин3, арсений мяуткин]
    // двоечник, двоечник, 2 2 3 2 2;1вася, пупкин, 3 4 5 6;арсений, мяуткин, 5 4 1 2;2вася, пупкин2, d f a
    // [1вася пупкин, арсений мяуткин, двоечник двоечник]
    fun task65() {
        val debug = true
        print(
            readlnOrNull().let {
                if (it == null) {
                    println("input error")
                    return
                }
                return@let it
            }.split(";")
                .map { student_info ->
                    student_info.split(",")
                }.filter { student_info ->
                    student_info.size == 3
                }
                .map { student_info ->
                    student_info[0] + student_info[1] to student_info[2].split(" ").mapNotNull { it.toIntOrNull() }.average()
                }
                .filterNot { it.second.isNaN() }
//                .sortedWith(Comparator<Pair<String, Double>> { o1, o2 ->  o1.second.compareTo(o2.second)}.reversed()
//                    .thenComparator { o1, o2 ->  o1.first.compareTo(o2.first)})
                .groupBy { it.second }
                .let {
                    if (debug) {
                        println(it)
                    }
                    return@let it
                }
                .let {
                    return@let it.keys.sorted().reversed().take(3).flatMap { avg_mark ->
                        it.get(avg_mark).orEmpty().map { it.first }.sorted()
                    }
                }

        )
    }


    fun sum1(func1: (Int) -> Int, func2: (Int) -> Int){
        listOf(10).fold(listOf<Int>()) { acc, value -> acc.plus(value) }
    }
    // 6 -> 2 (тк 3! = 6 что не меньше 6) 7 -> 3 (3! = 6 < 7)
    //
    fun task66() {
        print(
            readlnOrNull()?.toIntOrNull().let {
                if (it == null) {
                    println("input error")
                    return
                }
                return@let (1..it).map { digit ->
                    digit to it
                }
            }.takeWhile { (1..it.first)
                .reduce { acc, i -> acc * i } < it.second }
                .last().first
        )
    }
}