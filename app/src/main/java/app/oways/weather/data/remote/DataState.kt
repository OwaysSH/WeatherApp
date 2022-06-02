package app.oways.weather.data.remote

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class GenericError(val throwable: Throwable) : DataState<Nothing>()
    data class NetworkError(val throwable: Throwable) : DataState<Nothing>()
    data class NoResult(val query: String) : DataState<Nothing>()
    object Empty : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}