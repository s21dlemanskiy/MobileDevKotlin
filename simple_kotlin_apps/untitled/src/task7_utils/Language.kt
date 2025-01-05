package task7_utils

class Language (
    private val name: String,
    private val oop: OOP = OOP.NOT_PRESENT,
    private val type: LanguageType?
) {
    fun getFirstLetterOfName(): Char {
        return name.firstOrNull() ?: ' '
    }
    constructor(name: String, oop: String?, type: String?) : this(
        name,
        if(oop == null) OOP.NOT_PRESENT else  OOP.valueOf(oop),
        type?.let { LanguageType.valueOf(it) })

    override fun toString(): String {
        return "Language(name=${name}, oop=${oop}, type=${type})"
    }
}