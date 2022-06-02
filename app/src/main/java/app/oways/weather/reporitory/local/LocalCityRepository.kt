package app.oways.weather.reporitory.local

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.oways.weather.data.remote.DataState
import app.oways.weather.data.local.doa.CityDAO
import app.oways.weather.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEmpty

class LocalCityRepository(private val cityDoa: CityDAO): ILocalCityOperations {

    override suspend fun addCity(city: CityEntity) {
        cityDoa.insertCity(city)
    }

    override suspend fun deleteCity(cityId: Long) {
        cityDoa.deleteCity(cityId)
    }

    override suspend fun deleteCityByName(cityName: String) {
        cityDoa.deleteCityByName(cityName)

    }
    override suspend fun findCityByName(cityName: String): CityEntity? {
        return cityDoa.findCityByName(cityName)
    }

    override suspend fun findCityById(cityId: Long): CityEntity? {
        return cityDoa.findCityById(cityId)
    }

    override fun getAllCities(): LiveData<List<CityEntity>?> {
        return cityDoa.getAllCities()
    }

    override fun getSelectedCitiesPagingSource(orderBy: String?): Flow<DataState<Flow<PagingData<CityEntity>>>> =
        flow {
            emit(DataState.Loading)
            emit(
                DataState.Success(
                    Pager(
                        config = PagingConfig(pageSize = 10),
                        pagingSourceFactory = cityDoa.getSelectedCitiesPagingSource().asPagingSourceFactory()
                    ).flow.onEmpty {
                        emit(DataState.Empty)
                    }
                )
            )
        }
}