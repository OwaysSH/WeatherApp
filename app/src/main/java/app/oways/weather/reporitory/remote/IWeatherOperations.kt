package app.oways.weather.reporitory.remote

import app.oways.weather.data.remote.DataState
import app.oways.weather.data.remote.ForecastResponse
import app.oways.weather.data.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherOperations {

    suspend fun getCurrentCityWeather(cityName: String, units: String): Flow<DataState<WeatherResponse?>>
    suspend fun getCityForecast(cityName: String, units: String): Flow<DataState<ForecastResponse?>>

}