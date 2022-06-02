package app.oways.weather.data.local.doa

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.oways.weather.data.local.entity.CityEntity

@Dao
interface CityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(cityEntity: CityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(list: List<CityEntity>)

    @Query("SELECT * FROM cities where city_name =:cityName limit 1")
    suspend fun findCityByName(cityName: String): CityEntity?

    @Query("SELECT * FROM cities where id =:cityId limit 1")
    suspend fun findCityById(cityId: Long): CityEntity?

    @Query("Delete from cities where id =:cityId")
    suspend fun deleteCity(cityId: Long)

    @Query("Delete from cities where city_name =:cityName")
    suspend fun deleteCityByName(cityName: String)

    @Query("DELETE FROM cities")
    suspend fun deleteAllCity()

    @Query("SELECT * FROM cities")
    fun getAllCities(): LiveData<List<CityEntity>?>

    @Query("SELECT * FROM cities ORDER BY id ASC")
    fun getSelectedCitiesPagingSource(): DataSource.Factory<Int, CityEntity>

}