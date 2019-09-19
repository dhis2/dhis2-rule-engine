package org.hisp.dhis.rules.utils


actual class Date actual constructor(): Comparable<Date>{
    fun get() = kotlin.js.Date()
    fun after(date: Date): Boolean { TODO("not implemented")  }
    fun before(date: Date): Boolean { TODO("not implemented")  }

    override fun compareTo(other: Date): Int {
        TODO("not implemented")
    }
}

actual interface Callable<V> {
    actual fun call(): V
}

actual class SimpleDateFormat actual constructor(pattern: String) {
    actual fun applyPattern(pattern: String) {
        TODO("not implemented")
    }

    actual fun format(date: Date): String {
        TODO("not implemented")
    }
}

actual class DecimalFormat actual constructor(pattern: String) {
    actual fun format(result: Double): String {
        TODO("not implemented")
    }
}