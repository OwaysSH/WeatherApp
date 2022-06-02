package app.oways.weather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo("city_name")
    val name: String? = null,
    @ColumnInfo("country_name")
    val countryName: String? = null
)
