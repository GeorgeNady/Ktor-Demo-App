package com.george.ktorapp.utiles

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.george.ktorapp.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Preferences(context: Context) {

    companion object {

        private const val PREFS_NAME = "MySharedPref"
        const val TOKEN_PREF = "token"

        // we can use this Singleton object of Prefs and use from anywhere within the app.
        val prefs: Preferences by lazy {
            Preferences(MyApplication.instance)
        }

    }

    private val gson = Gson()

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun <T> getFromSharedPrefs( key: String,defValue: T): T {
        val result = when (defValue) {
            is Int -> sharedPrefs.getInt(key, defValue) as T
            is String -> sharedPrefs.getString(key, defValue) as T
            is Boolean -> sharedPrefs.getBoolean(key, defValue) as T
            is Float -> sharedPrefs.getFloat(key, defValue) as T
            is Long -> sharedPrefs.getLong(key, defValue) as T
            is MutableSet<*> -> sharedPrefs.getStringSet(key, setOf()) as T
            else -> {
                val jsonString = sharedPrefs.getString(key, "")
                gson.fromJson(jsonString, object : TypeToken<T>() {}.type)
            }
        }
        return result as T
    }

    var prefsToken: String
        get() = sharedPrefs.getString(TOKEN_PREF, "") ?: ""
        set(value) = sharedPrefs.edit { putString(TOKEN_PREF, value) }

    // var myObject: MyObject?
    //     get() {
    //         val jsonString = sharedPrefs.getString(KEY_USER_DATA, null) ?: return null
    //         return gson.fromJson(jsonString, object : TypeToken<MyObject>() {}.type)
    //     }
    //     set(value) = sharedPrefs.edit { putString(KEY_MY_OBJECT, gson.toJson(value)) }


}