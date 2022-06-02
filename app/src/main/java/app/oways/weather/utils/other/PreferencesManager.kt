package app.oways.weather.utils.other

import android.content.SharedPreferences

class PreferencesManager {

    private var mSharedPreferences: SharedPreferences? = null

    constructor(mSharedPreferences: SharedPreferences) {
        this.mSharedPreferences = mSharedPreferences
    }

    fun clear() {
        mSharedPreferences!!.edit().clear().apply()
    }

    fun saveTempType(tempType: String?) {
        val editor = mSharedPreferences?.edit()
        editor?.putString("temp_type", tempType)
        editor?.apply()
    }

    fun getTempType(): String {
        return mSharedPreferences?.getString("temp_type", "metric")?:"metric"
    }

}
