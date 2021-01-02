package id.smartech.get_talent.util

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(private val context: Context?) {
    private val PREF_NAME = "LOGIN"
    private val sharedPref : SharedPreferences
    val editor: SharedPreferences.Editor

    init {
        sharedPref = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

//    STRING
    fun put(key: String?, value: String?) {
        editor.putString(key, value)
            .apply()
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

//    BOOLEAN
    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun put(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

//    INTEGER
    fun getInteger(key: String): Int {
        return sharedPref.getInt(key, 0)
}

    fun put(key: String, value: Int) {
        editor.putInt(key, value)
            .apply()
    }

//    CLEAR
    fun clear() {
        editor.clear()
            .apply()
    }

//


}