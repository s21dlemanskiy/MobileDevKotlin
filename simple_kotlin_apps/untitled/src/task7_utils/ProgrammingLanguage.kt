package task7_utils

// Базовый класс для языков программирования
open class ProgrammingLanguage(val name: String, val paradigm: LanguageType) {
    open fun displayInfo(): String {
        return "Язык: $name, Парадигма: $paradigm"
    }
}