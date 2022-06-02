package app.oways.weather.di

import android.content.Context
import app.oways.weather.BuildConfig
import app.oways.weather.reporitory.remote.IWeatherServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext context: Context) =
        Cache(context.cacheDir, 10L * 1024 * 1024) // 10 MB

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClientCached(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(
            HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
        )
        client.addInterceptor {
            val original = it.request()
            val builder = original.newBuilder()
            builder.header("Content-Type", "application/json")
            builder.header("Accept", "application/json")
            val request = builder.build()
            val response = it.proceed(request)
            response.cacheResponse
            response
        }
        client.connectTimeout(120, TimeUnit.SECONDS)
        client.readTimeout(120, TimeUnit.SECONDS)
        client.callTimeout(120, TimeUnit.SECONDS)
        client.writeTimeout(120, TimeUnit.SECONDS)
        client.cache(cache)
        return client.build()
    }

    @Provides
    @Singleton
    @Named("default_retrofit")
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_API_ADDRESS)
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideMoviesService(@Named("default_retrofit") retrofit: Retrofit): IWeatherServices =
        retrofit.create(IWeatherServices::class.java)

}
