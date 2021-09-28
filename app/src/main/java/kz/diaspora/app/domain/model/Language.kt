package kz.diaspora.app.domain.model

import kz.diaspora.app.R
import java.io.Serializable

class Language(icon: Int?, name: String, code: String) : Serializable {

    var icon: Int? = icon
    var name: String? = name
    var code: String? = code

    companion object {

        fun getLanguageList(): ArrayList<Language> {
            val list = ArrayList<Language>()
            list.add(Language(null,R.string.Choose_your_language.toString(), "en"))
            list.add(Language(R.drawable.ic_america,"English", "en"))
            list.add(Language(R.drawable.ic_russian, "Русский", "ru"))
            return list
        }
    }
}