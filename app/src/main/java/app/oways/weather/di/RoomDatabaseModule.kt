package app.oways.weather.di

import android.app.Application
import androidx.room.Room
import app.oways.weather.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application) =
        Room.databaseBuilder(application, AppDatabase::class.java, "weather_owa_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesCityDAO(appDatabase: AppDatabase) = appDatabase.cityDAO()

}