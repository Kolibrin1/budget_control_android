package com.example.budgetcontrolandroid.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

fun Instant.toFormattedDate(): String {
    val localDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return localDate.toJavaLocalDate().format(formatter)
}