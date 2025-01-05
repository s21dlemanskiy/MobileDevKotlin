package task7_utils

// Класс для управления языками
class LanguageManager {
    private val languages = mutableListOf<ProgrammingLanguage>()

    fun addLanguage(language: ProgrammingLanguage) {
        languages.add(language)
    }

    fun displayLanguagesStartingWithA() {
        for (lang in languages) {
            if (lang.name.startsWith('A')) {
                println(lang.displayInfo())
            }
        }
    }
}