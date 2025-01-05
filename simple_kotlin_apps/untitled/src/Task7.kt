import task7_utils.AlgoritmLanguage
import task7_utils.LanguageManager
import task7_utils.LanguageType
import task7_utils.OOP
import task7_utils.ProgrammingLanguage

class Task7 {
    fun task7() {
//        val languages = mutableListOf<Language>();
//        while (true) {
//            print("continue input languages(y/n):")
//            val addNewLanguage = arrayOf("y", "yes").contains(readln().lowercase())
//            if (!addNewLanguage) {
//                println("Stop language input")
//                break
//            }
//            print("print name of language:")
//            val name = readln()
//            print("What oop in language (${OOP.entries}):")
//            val oop = readln()
//            print("What is type of language (${LanguageType.entries}):")
//            val type = readln()
//            languages.add(Language(name=name, oop = oop, type =  type))
//        }
//        println("все языки")
//        println(languages)
//        println("начинающие а")
//        println(languages.filter { it.getFirstLetterOfName().lowercase() == "а" })

        val manager = LanguageManager()

        // Ввод информации о языках
        manager.addLanguage(ProgrammingLanguage("Ada", LanguageType.STRUCT))
        manager.addLanguage(AlgoritmLanguage("JavaScript", OOP.PROTOTYPE))
        manager.addLanguage(AlgoritmLanguage("Python", OOP.CLASSES))

        // Вывод информации о языках, начинающихся на "А"
        println("Языки, начинающиеся на 'А':")
        manager.displayLanguagesStartingWithA()
    }

}