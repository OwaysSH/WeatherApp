package app.oways.weather.reporitory.remote

import app.oways.weather.data.remote.ForecastResponse
import app.oways.weather.data.remote.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherServices {

    @GET("weather")
    suspend fun getCurrentCityWeather(@Query("q") city: String, @Query("appid") appId: String,
                                      @Query("lang") lang: String,
                                      @Query("units") units: String? = "metric"): Response<WeatherResponse?>

    @GET("forecast")
    suspend fun getCityForecast(@Query("q") city: String, @Query("appid") appId: String,
                                     @Query("lang") lang: String,
                                     @Query("units") units: String? = "metric"): Response<ForecastResponse?>

}