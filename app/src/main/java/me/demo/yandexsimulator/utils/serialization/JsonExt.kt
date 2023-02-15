package me.demo.yandexsimulator.utils.serialization

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlin.LazyThreadSafetyMode.NONE

val Json.Default.actual: Json by lazy(NONE) {
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
        allowSpecialFloatingPointValues = true
        useArrayPolymorphism = true
    }
}

fun <T> Json.toJson(serializer: SerializationStrategy<T>, value: T?): JsonElement =
    if (value == null) JsonNull else toJson(serializer, value)

fun JsonObject.Companion.create(vararg pair: Pair<String, JsonElement?>): JsonObject =
    create(pair.toMap())

fun JsonObject.Companion.create(content: Map<String, JsonElement?>): JsonObject {
    val result: MutableMap<String, JsonElement> = hashMapOf()

    for (entry: Map.Entry<String, JsonElement?> in content) {
        if (entry.value == null) continue
        result[entry.key] = checkNotNull(entry.value)
    }

    return JsonObject(result)
}