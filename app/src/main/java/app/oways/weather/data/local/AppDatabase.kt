package app.oways.weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.oways.weather.data.local.doa.CityDAO
import app.oways.weather.data.local.entity.CityEntity

@Database(
    entities = [
        CityEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDAO(): CityDAO
}