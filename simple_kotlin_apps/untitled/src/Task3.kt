class Task3 {
//    1. С клавиатуры вводится описание массива из 10 элементов в виде: номер :значение
//    однако, порядок указания элементов может быть любой. Выведите все элементы мас- сива в порядке возрастания номеров.
    // 1:2 2:3 3:4 4:5 5:6 6:7 7:8 8:9 9:0 0:1 -> 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
    fun main(){
        val arr = arrayOfNulls<Long>(10)
        var i = 0
        while (i < 10) {
            val inputVal = readlnOrNull()
            if (inputVal == null) {
                println("wrong input, try again")
                continue
            }
            if (inputVal.length <= 2) {
                println("wrong input, try again")
                continue
            }
            val index = inputVal[0].digitToIntOrNull()
            if (index == null || inputVal[1] != ':') {
                println("wrong input, try again")
                continue
            }
            val value = inputVal.slice( 2..<inputVal.length).toLongOrNull()
            if (value == null) {
                println("wrong input, try again")
                continue
            }
            arr[index] = value
            i += 1
        }
//        arr.sort()
        println(arr.joinToString())
    }
}