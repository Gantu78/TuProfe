package com.example.tuprofe.data.language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale


object LanguageManager {

    private const val PREFS_NAME = "language_prefs"
    private const val PREF_KEY   = "selected_language"

    val SUPPORTED = listOf("", "es", "en", "fr", "pt", "ar", "de", "it", "ja", "ko", "ru", "hi", "th", "vi", "zh", "nl", "pl", "sv", "tr")

    fun getSaved(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PREF_KEY, "") ?: ""

    fun save(context: Context, code: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_KEY, code)
            .apply()
    }

    fun applyToContext(base: Context): Context {
        val code = getSaved(base)
        if (code.isEmpty()) return base          // sistema por defecto
        val locale = Locale(code)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}
