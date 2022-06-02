package app.oways.weather.utils.other

import app.oways.weather.utils.extentions.getCurrentTimeInMillis
import java.text.SimpleDateFormat
import java.util.*

object DateUtility {

    @JvmStatic
    fun getCurrentDate(dateTime: Long?, timezone: Long?): String = try {

        val dateFormat = SimpleDateFormat("E MMMM dd yyyy, HH:mm", Locale.getDefault()).apply { timeZone = TimeZone.getDefault() }
        dateFormat.format(Date((dateTime?: getCurrentTimeInMillis())*1000-((timezone?:0)*1000))
        )
    } catch (e: Exception) {
        ""
    }
    @JvmStatic
    fun getCurrentDateShort(dateTime: Long?, timezone: Long?): String = try {

        val dateFormat = SimpleDateFormat("E MMMM dd, HH:mm", Locale.getDefault()).apply { timeZone = TimeZone.getDefault() }
        dateFormat.format(Date((dateTime?: getCurrentTimeInMillis())*1000-((timezone?:0)*1000))
        )
    } catch (e: Exception) {
        ""
    }
}