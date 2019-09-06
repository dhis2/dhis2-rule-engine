package org.hisp.dhis.rules


fun String?.extSubstring(start: Int, end: Int?): String? {
    var start = start
    var end = end?.let { it } ?: return null

    when {
        this == null -> return null
        end < 0 -> end += this.length
        start < 0 -> start += this.length
        end > this.length -> end = this.length
    }

    if (start > end)  return ""

    if (start < 0)
        start = 0

    if (end < 0)
        end = 0

    return this?.substring(start, end)
}


fun String?.toDouble(defaultValue: Double): Double {
    return when (this) {
        null -> defaultValue
        else -> try {
            this.toDouble()
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }
}

fun String?.wrap(): String {
    return if (this == null) "" else "'$this'"
}