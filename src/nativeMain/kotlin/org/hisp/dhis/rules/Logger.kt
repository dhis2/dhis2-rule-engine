package org.hisp.dhis.rules

actual fun createLogger(className: String): Logger {
    return Logger({ message -> println(message) }, { message: String -> println(message) })
}