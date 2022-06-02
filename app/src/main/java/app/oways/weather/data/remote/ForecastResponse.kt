package app.oways.weather.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastResponse(

    @SerializedName("list")
    val list: ArrayList<WeatherResponse>? = null,
    @SerializedName("city")
    val city: CityResponse? = null,

): Parcelable


@Parcelize
data class CityResponse(
    @SerializedName("id")
    val id: Long? = 0,
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("country")
    val country: String? = "",
): Parcelable