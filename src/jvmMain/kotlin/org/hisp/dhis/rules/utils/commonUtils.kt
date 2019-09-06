package org.hisp.dhis.rules.utils

import java.util.concurrent.Callable

actual typealias Date = java.util.Date

actual fun Date.compareTo(date: Date): Int {
    return this.compareTo(date)
}

actual typealias Callable<V> = Callable<V>
