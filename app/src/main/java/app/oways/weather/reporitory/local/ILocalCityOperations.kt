package app.oways.weather.reporitory.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import app.oways.weather.data.remote.DataState
import app.oways.weather.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

interface ILocalCityOperations {

    suspend fun addCity(city: CityEntity)

    suspend fun deleteCity(cityId: Long)

    suspend fun deleteCityByName(cityName: String)

    suspend fun findCityByName(cityName: String): CityEntity?

    suspend fun findCityById(cityId: Long): CityEntity?

    fun getAllCities(): LiveData<List<CityEntity>?>

    fun getSelectedCitiesPagingSource(orderBy: String?): Flow<DataState<Flow<PagingData<CityEntity>>>>
}