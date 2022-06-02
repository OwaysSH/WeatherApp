package app.oways.weather.di

import app.oways.weather.data.local.doa.CityDAO
import app.oways.weather.reporitory.local.ILocalCityOperations
import app.oways.weather.reporitory.local.LocalCityRepository
import app.oways.weather.reporitory.remote.IWeatherOperations
import app.oways.weather.reporitory.remote.IWeatherServices
import app.oways.weather.reporitory.remote.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideWeatherRepository(serviceApi: IWeatherServices): IWeatherOperations {
        return WeatherRepository(serviceApi)
    }


    @Provides
    fun provideLocalCityRepository(dao: CityDAO): ILocalCityOperations {
        return LocalCityRepository(dao)
    }
}