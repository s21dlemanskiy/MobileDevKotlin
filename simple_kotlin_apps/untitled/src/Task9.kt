class Task9 {
    // Интерфейс наушников
    interface Headphones {
        val manufacturer: String
        val model: String
        val hasNoiseCancellation: Boolean
        val price: Double

        fun getDescription(): String
    }

    // Базовый класс для обычных наушников
    class BasicHeadphones(
        override val manufacturer: String,
        override val model: String,
        override val hasNoiseCancellation: Boolean,
        override val price: Double
    ) : Headphones {
        override fun getDescription(): String {
            return "$manufacturer $model, Noise Cancellation: $hasNoiseCancellation, Price: $price"
        }
    }

    // Декоратор для беспроводных наушников
    class WirelessHeadphones(
        private val baseHeadphones: Headphones,
        val range: Int,
        val isDigitalSignal: Boolean
    ) : Headphones by baseHeadphones {

        override fun getDescription(): String {
            return "${baseHeadphones.getDescription()}, Range: $range m, Digital Signal: $isDigitalSignal"
        }
    }

    // Декоратор для вставных наушников
    class InEarHeadphones(
        private val baseHeadphones: Headphones,
        numberOfEarpads: Int,
        val hasVolumeControl: Boolean
    ) : Headphones by baseHeadphones {
        val numberOfEarpads1: Int
        init {
            numberOfEarpads1 = numberOfEarpads;
        }

        override fun getDescription(): String {
            return "${baseHeadphones.getDescription()}, Earpads: $numberOfEarpads1, Volume Control: $hasVolumeControl"
        }
    }

    // Функция для фильтрации наушников по цене
    fun filterExpensiveHeadphones(headphonesList: List<Headphones>, minPrice: Double): List<Headphones> {
        return headphonesList.filter { it.price > minPrice }
    }

    // Пример использования
    fun task9() {
        val headphonesList = mutableListOf<Headphones>()

        headphonesList.add(BasicHeadphones("Sony", "WH-1000XM4", true, 4999.0))
        headphonesList.add(BasicHeadphones("Xunsunjun", "SuperMegaWortexBortex", false, 300.0))
        headphonesList.add(WirelessHeadphones(BasicHeadphones("Apple", "AirPods Pro", true, 24990.0), 10, true))
        headphonesList.add(InEarHeadphones(BasicHeadphones("Samsung", "Galaxy Buds", false, 10990.0), 3, true))
        headphonesList.add(BasicHeadphones("Xiaomi", "Redmi AirDots", false, 2499.0))

        val expensiveHeadphones = filterExpensiveHeadphones(headphonesList, 500.0)

        println("Наушники стоимостью больше 500 рублей:")
        for (headphones in expensiveHeadphones) {
            println(headphones.getDescription())
        }
    }
}