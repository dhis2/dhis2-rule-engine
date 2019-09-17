package org.hisp.dhis.rules.utils


expect class Date()

expect fun Date.compareTo(date: Date): Int

expect interface Callable<V> {
    fun call() : V
}

expect class SimpleDateFormat() {
    fun applyPattern(pattern: String)
    fun format(date: Date): String
}

expect class DecimalFormat(pattern: String) {
    fun format(result: Double): String
}