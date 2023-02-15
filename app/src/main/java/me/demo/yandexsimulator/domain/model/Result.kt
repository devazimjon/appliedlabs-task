package me.demo.yandexsimulator.domain.model

import retrofit2.Response
import java.io.IOException

sealed class Result<out U : Any>
data class SuccessResult<out T : Any>(val data: T) : Result<T>()
data class Failure(val httpError: HttpError) : Result<Nothing>()

class HttpError(val throwable: Throwable, val errorCode: Int = 0)

inline fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is SuccessResult) action(data)
    return this
}

inline fun <T : Any> Result<T>.onFailure(action: (HttpError) -> Unit) {
    if (this is Failure) action(httpError)
}


inline fun <T : Any> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
    if (isSuccessful) this.body()?.run(action)
    return this
}

inline fun <T : Any> Response<T>.onFailure(action: (HttpError) -> Unit) {
    if (isSuccessful.not())
        action(HttpError(Throwable(message()), code()))
}

// todo only for void requests
fun <T : Any> Response<T>.getOptionalData(): Result<T> {
    try {
        onSuccess { return SuccessResult(it) }
        onFailure { return Failure(it) }
        return Failure(HttpError(Throwable("Internet connection error")))
    } catch (e: IOException) {
        return Failure(HttpError(Throwable("Internet connection error")))
    }
}