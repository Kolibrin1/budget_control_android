package com.example.budgetcontrolandroid.domain.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.Instant
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