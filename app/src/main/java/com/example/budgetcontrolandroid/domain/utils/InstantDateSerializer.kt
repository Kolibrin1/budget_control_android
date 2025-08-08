package com.example.budgetcontrolandroid.domain.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class InstantDateDeserializer: JsonDeserializer<Instant> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? {
        if (json?.isJsonNull == true) return null
        val dateStr = json?.asString ?: return null
        return LocalDateTime.parse(dateStr, formatter).atZone(ZoneOffset.UTC).toInstant().toKotlinInstant()
    }
}

class InstantDateSerializer : JsonSerializer<Instant> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    override fun serialize(
        src: Instant?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        if (src == null) return null
        val javaInstant = src.toJavaInstant()
        val localDateTime = javaInstant.atZone(ZoneOffset.UTC).toLocalDateTime()
        val formattedDate = localDateTime.format(formatter)
        return JsonPrimitive(formattedDate)
    }
}