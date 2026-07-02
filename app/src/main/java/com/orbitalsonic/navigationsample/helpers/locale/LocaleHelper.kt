package com.orbitalsonic.navigationsample.helpers.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun applyLanguage(languageCode:String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}