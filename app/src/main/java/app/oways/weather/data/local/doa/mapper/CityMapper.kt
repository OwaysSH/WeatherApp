package app.oways.weather.data.local.doa.mapper

import app.oways.weather.data.remote.WeatherResponse
import app.oways.weather.data.local.entity.CityEntity

object CityMapper {

    fun transform(weatherResponse: WeatherResponse): CityEntity {

        return CityEntity(
            id = weatherResponse.cityId,
            name = weatherResponse.cityName,
            countryName = weatherResponse.countrySysInfo?.country
        )
    }

}