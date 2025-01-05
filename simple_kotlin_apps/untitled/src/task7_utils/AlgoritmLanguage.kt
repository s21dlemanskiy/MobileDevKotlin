package task7_utils

// Класс для объектно-ориентированных языков
class AlgoritmLanguage(
    name: String,
    private val oopType: OOP
) : ProgrammingLanguage(name, LanguageType.ALGORITHMIC) {
    override fun displayInfo(): String {
        return "${super.displayInfo()} (OOP type: ${oopType})"
    }
}
