package com.example.pucquiz.shared

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun formatTimeToString(timeInMillis: Long, pattern: String): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"))
        calendar.timeInMillis = timeInMillis
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT-3")
        return sdf.format(calendar.time)
    }
    fun nowInMillis() = Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
    fun localDayStartMillis() = Calendar.getInstance(TimeZone.getDefault()).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    fun localDayEndMillis() = Calendar.getInstance(TimeZone.getDefault()).apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, getMaximum(Calendar.MILLISECOND))
    }.timeInMillis
    fun nowInCalendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    fun now() = nowInCalendar().time
    fun toCalendar(timeInMillis: Long): Calendar {
        val nowInCalendar = nowInCalendar()
        nowInCalendar.timeInMillis = timeInMillis
        return nowInCalendar
    }
    fun toDate(timeInMillis: Long): Date {
        return toCalendar(timeInMillis).time
    }
    fun toDate(dateStr: String, pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"): Date {
        return SimpleDateFormat(pattern).parse(dateStr)
    }
    fun isSameDay(date1: Date, date2: Date): Boolean {
        val fmt = SimpleDateFormat("yyyyMMdd")
        return fmt.format(date1) == fmt.format(date2)
    }
}
const val ONE_HOUR = 60 * 60 * 1000
const val ONE_DAY = 24 * ONE_HOUR

fun Long.sumHours(period: Int?): Long {
    val hours = period ?: 0
    return this + hours.hoursToMillis()
}
fun Long.sumDays(period: Int?): Long {
    val days = period ?: 0
    return this + days.daysToMillis()
}
fun Long.toDays(): Int = (this / ONE_DAY).toInt()
fun Long.subtractHours(period: Int): Long = this.sumHours(-period)
fun Long.subtractDays(period: Int): Long = this.sumDays(-period)
fun Int.hoursToMillis(): Long = this.toLong() * ONE_HOUR
fun Int.daysToMillis(): Long = this.toLong() * ONE_DAY