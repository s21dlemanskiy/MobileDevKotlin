class Task52 {
    //Создайте функцию, которая по данному массиву целых чисел возвращает функцию,
    //которая при каждом вызове последовательно возвращает элементы массива, а когда элементы кончатся – null.
    fun main() {
        val arr = arrayOf(0, 1, 2, 3)
        val getElem = getFunc(arr)
        val arr2 = arrayOf(0, 1, 2, 3)
        val getElem2 = getFunc(arr)
        println(getElem2())
        println(getElem2())
        println(getElem2())
        println(getElem2())
        println(getElem2())
        println(getElem2())
        println(getElem2())
        println(getElem())
        println(getElem())
        println(getElem())
        println(getElem())
        println(getElem())
        println(getElem())
        println(getElem())

    }

    fun getFunc(arr: Array<Int>): () -> Int? {
        var index = 0
        return { arr.getOrNull(index++) }
    }
}