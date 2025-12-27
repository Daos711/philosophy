package com.example.philoquiz.data

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var isDarkTheme: Boolean
        get() = prefs.getBoolean("dark_theme", false)
        set(value) = prefs.edit().putBoolean("dark_theme", value).apply()

    var fontSize: Int
        get() = prefs.getInt("font_size", 16) // default 16sp
        set(value) = prefs.edit().putInt("font_size", value.coerceIn(12, 24)).apply()
}
