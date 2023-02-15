package me.demo.yandexsimulator.data.network.rest.converter

import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal object CustomConvertersFactory : Converter.Factory() {
    private val requestConverters: Array<CustomConverter<*, RequestBody>> =
        arrayOf(JsonRequestConverter)
    private val responseConverters: Array<CustomConverter<ResponseBody, *>> =
        arrayOf(UnitResponseConverter)

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? =
        requestConverters.find { it.type == type }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? =
        responseConverters.find { it.type == type }

    private object JsonRequestConverter : CustomConverter<JsonElement, RequestBody> {
        override val type: Type = JsonElement::class.java
        override fun convert(value: JsonElement): RequestBody =
            value.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    private object UnitResponseConverter : CustomConverter<ResponseBody, Unit> {
        override val type = Unit::class.java
        override fun convert(value: ResponseBody): Unit = value.close()
    }

    private interface CustomConverter<F, T> : Converter<F, T> {
        val type: Type
    }
}
