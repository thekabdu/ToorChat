package kz.diaspora.app.domain.model

import kz.diaspora.app.R
import java.io.Serializable

class Language( name: String, code: String) : Serializable {


    var name: String? = name
    var code: String? = code

    companion object {

        fun getLanguageList(): ArrayList<Language> {
            val list = ArrayList<Language>()
            list.add(Language(R.string.Choose_your_language.toString(), "en"))
            list.add(Language("English", "en"))
            list.add(Language("Русский", "ru"))
            return list
        }
    }
}