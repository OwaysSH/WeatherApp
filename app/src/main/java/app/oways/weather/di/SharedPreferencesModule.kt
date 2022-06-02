package app.oways.weather.di

import android.content.Context
import android.content.SharedPreferences
import app.oways.weather.utils.other.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("WEATHER_APP_PREFS", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAppSharedPreferences(sharedPreferences: SharedPreferences) =
        PreferencesManager(sharedPreferences)
}

