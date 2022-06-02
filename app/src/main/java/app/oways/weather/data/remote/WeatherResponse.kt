package app.oways.weather.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherResponse(
    @SerializedName("timezone")
    val timezone: Long? = 0,
    @SerializedName("dt")
    val dateTime: Long? = null,
    @SerializedName("id")
    val cityId: Long? = null,
    @SerializedName("name")
    val cityName: String? = null,
    @SerializedName("main")
    val mainTemp: MainTemp? = null,
    @SerializedName("sys")
    val countrySysInfo: CountrySysInfo? = null,
    @SerializedName("weather")
    val weather: ArrayList<Weather>? = null,
    @SerializedName("dt_txt")
    val dateTxt: String? = null,

    var isSelectedCity: Boolean? = false
) : Parcelable


@Parcelize
data class CountrySysInfo(
    @SerializedName("type")
    val type: Long? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("sunrise")
    val sunrise: Long? = null,
    @SerializedName("sunset")
    val sunset: Long? = null): Parcelable


@Parcelize
data class Weather(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("main")
    val main: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null
) : Parcelable

@Parcelize
data class MainTemp(
    @SerializedName("temp")
    val temp: Float? = null,
    @SerializedName("feels_like")
    val feels_like: Float? = null,
    @SerializedName("temp_min")
    val temp_min: Float? = null,
    @SerializedName("temp_max")
    val temp_max: Float? = null,
    @SerializedName("pressure")
    val pressure: Float? = null,
    @SerializedName("humidity")
    val humidity: Float? = null
):Parcelable