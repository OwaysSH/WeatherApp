package app.oways.weather.reporitory.remote

import app.oways.weather.BuildConfig
import app.oways.weather.data.remote.DataState
import app.oways.weather.data.remote.ForecastResponse
import app.oways.weather.data.remote.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val serviceApi: IWeatherServices) :
    IWeatherOperations {


    override suspend fun getCurrentCityWeather(cityName: String, units: String): Flow<DataState<WeatherResponse?>> =
        flow {

            try {
                emit(DataState.Loading)
                val result = serviceApi.getCurrentCityWeather(
                    cityName,
                    BuildConfig.API_KEY,
                    "en",
                    units
                )
                if (result.isSuccessful) {
                    result.body()?.let {
                        emit(DataState.Success(it))
                    } ?: emit(DataState.Empty)
                } else {
                    emit(DataState.Empty)
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> emit(DataState.NetworkError(throwable))
                    else -> emit(DataState.GenericError(throwable))
                }
            }
        }

    override suspend fun getCityForecast(cityName: String, units: String): Flow<DataState<ForecastResponse?>> =
        flow {

            try {
                emit(DataState.Loading)
                val result = serviceApi.getCityForecast(
                    cityName,
                    BuildConfig.API_KEY,
                    "en",
                    units
                )
                if (result.isSuccessful) {
                    result.body()?.let {
                        emit(DataState.Success(it))
                    } ?: emit(DataState.Empty)
                } else {
                    emit(DataState.Empty)
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> emit(DataState.NetworkError(throwable))
                    else -> emit(DataState.GenericError(throwable))
                }
            }
        }
}
