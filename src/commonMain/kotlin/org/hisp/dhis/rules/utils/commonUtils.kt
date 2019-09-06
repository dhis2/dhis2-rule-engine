package org.hisp.dhis.rules.utils


expect class Date()

expect fun Date.compareTo(date: Date): Int

expect interface Callable<V> {
    fun call() : V
}

expect class LocalDate() {

}
