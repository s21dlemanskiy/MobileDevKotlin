class Task10 {
    fun <T> createIterator(arr: Array<T>): () -> T? {
        var index = 0
        return { -> if(index < arr.size) arr[index++] else null}
    }

    fun task1() {
        val intArray = arrayOf(1, 2, 3, 4, 5)
        val stringArray = arrayOf("A", "B", "C")

        val intIterator = createIterator(intArray)
        val stringIterator = createIterator(stringArray)

        println("Integer Array:")
        for (i in 0..intArray.size) { // Вызовем на один раз больше, чтобы показать null
            println(intIterator())
        }

        println("\nString Array:")
        for (i in 0..stringArray.size) {
            println(stringIterator())
        }
    }
}