package kz.diaspora.app.utils

import android.content.Context
import android.content.res.Configuration
import kz.diaspora.app.data.db.PrefsImpl
import java.util.*

fun updateResources(context: Context?, language: String) : Context? {

    val locale = Locale(language)
    Locale.setDefault(locale)
    val res = context?.resources
    val config = Configuration(res?.configuration)
    config.setLocale(locale)
    return context?.createConfigurationContext(config)
}

fun setLocale(context: Context?) : Context? {
    val prefsImpl = context?.let { PrefsImpl(it) }
    val lang = prefsImpl?.getLanguage()
    return if (lang.isNullOrEmpty()) {
        context
    } else {
        updateResources(context, prefsImpl.getLanguage())
    }
}