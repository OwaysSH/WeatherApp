package app.oways.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import app.oways.weather.data.remote.DataState
import app.oways.weather.data.remote.WeatherResponse
import app.oways.weather.data.local.entity.CityEntity
import app.oways.weather.data.remote.ForecastResponse
import app.oways.weather.reporitory.local.ILocalCityOperations
import app.oways.weather.reporitory.remote.IWeatherOperations
import app.oways.weather.utils.other.PreferencesManager
import app.oways.weather.utils.other.TempType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: IWeatherOperations,
    private val localCityRepository: ILocalCityOperations,
    private val preferences: PreferencesManager
) : ViewModel() {


    private val _cityWeather: MutableLiveData<DataState<WeatherResponse?>> = MutableLiveData()
    val cityWeather: LiveData<DataState<WeatherResponse?>> get() = _cityWeather

    private val _cityForecast: MutableLiveData<DataState<ForecastResponse?>> = MutableLiveData()
    val cityForecast: LiveData<DataState<ForecastResponse?>> get() = _cityForecast

    val selectedDestinationsLiveData = localCityRepository.getAllCities()

    private val _selectedCities: MutableLiveData<DataState<Flow<PagingData<CityEntity>>>> =
        MutableLiveData()
    val selectedCities: LiveData<DataState<Flow<PagingData<CityEntity>>>> get() = _selectedCities


    private fun onWeatherEvent(event: WeatherEvents) {
        viewModelScope.launch {
            when (event) {
                is WeatherEvents.GetCityWeather -> {
                    val units = preferences.getTempType()
                    repository.getCurrentCityWeather(event.cityName, units).collectLatest { dataState ->
                        when (dataState) {
                            is DataState.Success -> {
                                dataState.data?.cityName?.let { cityName->
                                    val cityEntity = dataState.data.cityId?.let {
                                        localCityRepository.findCityById(
                                            it
                                        )
                                    }?:localCityRepository.findCityByName(
                                        cityName
                                    )
                                    if (cityEntity!=null){
                                        dataState.data.isSelectedCity = true
                                    }
                                    _cityWeather.postValue(dataState)
                                }?: _cityWeather.postValue(DataState.Empty)
                            }
                            is DataState.Empty ->{
                                _cityWeather.postValue(DataState.Empty)
                            }
                            is DataState.GenericError, is DataState.NetworkError -> {
                                (dataState as? DataState.GenericError)
                                    ?: (dataState as? DataState.NetworkError)
                            }
                        }
                    }
                }
                is WeatherEvents.GetCityForecast ->{
                    val units = preferences.getTempType()

                    repository.getCityForecast(event.cityName, units).collectLatest { dataState ->
                        when (dataState) {
                            is DataState.Success -> {
                                _cityForecast.postValue(dataState)
                            }
                            is DataState.GenericError, is DataState.NetworkError -> {
                                val throwable = (dataState as? DataState.GenericError)?.throwable
                                    ?: (dataState as? DataState.NetworkError)?.throwable
                            }
                        }
                    }
                }
            }
        }
    }

    fun getCityWeather(cityName: String) {
        onWeatherEvent(WeatherEvents.GetCityWeather(cityName))
    }
    fun getCityForecast(cityName: String) {
        onWeatherEvent(WeatherEvents.GetCityForecast(cityName))
    }

    fun addCity(city: CityEntity) {
        onCitiesEvent(CitiesEvents.InsertCity(city))
    }

    fun getSelectedCities() {
        onCitiesEvent(CitiesEvents.GetSelectedCities(null))
    }

    fun findCityByName(cityName: String) {
        onCitiesEvent(CitiesEvents.FindCityByName(cityName))
    }

    fun deleteCityById(cityId: Long) {
        onCitiesEvent(CitiesEvents.DeleteCityById(cityId))
    }
    fun deleteCityByCityName(cityName: String) {
        onCitiesEvent(CitiesEvents.DeleteCityByCityName(cityName))
    }

    private fun onCitiesEvent(event: CitiesEvents) {
        viewModelScope.launch {
            when (event) {
                is CitiesEvents.InsertCity -> {
                    localCityRepository.addCity(event.city)
                }
                is CitiesEvents.FindCityByName ->{
                    val cityResult = localCityRepository.findCityByName(event.cityName)


                }
                is CitiesEvents.GetSelectedCities -> {
                    localCityRepository.getSelectedCitiesPagingSource(event.orderBy)
                        .onEmpty {
                            _selectedCities.postValue(DataState.Empty)
                        }
                        .collectLatest {
                            _selectedCities.postValue(it)
                        }
                }
                is CitiesEvents.DeleteCityById -> {
                    localCityRepository.deleteCity(event.cityId)
                }
                is CitiesEvents.DeleteCityByCityName -> {
                    localCityRepository.deleteCityByName(event.cityName)
                }
            }
        }
    }

    fun getTempType(): String {
        return if (preferences.getTempType() == "metric"){
            TempType.CELSIUS.unit
        }else{
            TempType.FAHRENHEIT.unit
        }
    }
    fun switchTempType(): String {
        return if (preferences.getTempType() == "metric"){
            preferences.saveTempType("imperial")
            TempType.FAHRENHEIT.unit
        }else{
            preferences.saveTempType("metric")
            TempType.CELSIUS.unit
        }
    }
}

sealed class WeatherEvents {
    class GetCityWeather(val cityName: String) : WeatherEvents()
    class GetCityForecast(val cityName: String) : WeatherEvents()
}

sealed class CitiesEvents {
    class InsertCity(val city: CityEntity) : CitiesEvents()
    class FindCityByName(val cityName: String): CitiesEvents()
    class GetSelectedCities(val orderBy: String?) : CitiesEvents()
    class DeleteCityById(val cityId: Long) : CitiesEvents()
    class DeleteCityByCityName(val cityName: String) : CitiesEvents()


}