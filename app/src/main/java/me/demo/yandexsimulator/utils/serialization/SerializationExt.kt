package me.demo.yandexsimulator.utils.serialization

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder

fun <T> SerializationStrategy<T>.asJsonElement(format: Json = Json.actual, obj: T) =
    with(Json) { format.parseToJsonElement(encodeToString(this@asJsonElement, obj)) }

fun <T> SerializationStrategy<T>.asJsonElement(obj: T) =
    asJsonElement(Json.actual, obj)

fun Decoder.decodeJsonElement(): JsonElement =
    (this as JsonDecoder).decodeJsonElement()

fun Encoder.encodeJsonElement(jsonElement: JsonElement) =
    (this as JsonEncoder).encodeJsonElement(jsonElement)