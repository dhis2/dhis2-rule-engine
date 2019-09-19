package org.hisp.dhis.rules.utils

expect class Date(): Comparable<Date>

expect interface Callable<V> {
    fun call() : V
}

expect class SimpleDateFormat(pattern: String) {
    fun applyPattern(pattern: String)
    fun format(date: Date): String
}

expect class DecimalFormat(pattern: String) {
    fun format(result: Double): String
}