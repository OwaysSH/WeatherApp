package app.oways.weather.ui.adapter.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.oways.weather.R
import app.oways.weather.data.remote.WeatherResponse
import app.oways.weather.utils.other.DateUtility
import app.oways.weather.utils.extentions.load
import kotlinx.android.synthetic.main.forecast_item.view.*
import kotlin.collections.ArrayList

class ForecastAdapter(private val itemWidth : Int = ViewGroup.LayoutParams.MATCH_PARENT, private var tempType: String): RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private var list: ArrayList<WeatherResponse>? = null
    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            weather: WeatherResponse?,
            tempType: String
        ) {
            with(itemView) {
                weather?.apply {
                    temp_tv?.text =
                        String.format(resources.getString(R.string.temp_value), mainTemp?.temp?.toUInt(), tempType)

                    weather_desc_tv?.text = weather.weather?.get(0)?.description
                    current_date_tv?.text = "${
                        DateUtility.getCurrentDateShort(
                            dateTime,
                            timezone
                        )
                    }"
                    feels_like_temp_tv?.text = String.format(
                        resources.getString(R.string.feels_like),
                        mainTemp?.feels_like?.toUInt()
                    )
                    val iconUrl = "http://openweathermap.org/img/w/${weather.weather?.get(0)?.icon}.png"
                    max_min_temp_tv?.text = String.format(
                        resources.getString(R.string.max_min_temp),
                        mainTemp?.temp_max?.toUInt(),
                        mainTemp?.temp_min?.toUInt()
                    )
                    weather_icon_imageview?.load(iconUrl, R.drawable.ic_sunny_placeholder)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)

        view.layoutParams = RecyclerView.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        (view.layoutParams as RecyclerView.LayoutParams).bottomMargin  = parent.resources.getDimensionPixelSize(R.dimen.normal_gap)
        (view.layoutParams as RecyclerView.LayoutParams).marginEnd  = parent.resources.getDimensionPixelSize(R.dimen.normal_gap)

        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(list?.get(position), tempType)
    }

    override fun getItemCount() = list?.size ?: 0

    fun addList(data: ArrayList<WeatherResponse>) {
        list = data
        this.notifyDataSetChanged()
    }

    fun addTempType(tempType: String) {
        this.tempType = tempType
    }
}
