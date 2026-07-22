package org.hisp.dhis.rules

actual fun createLogger(className: String): Logger {
    return Logger(
        severe = { message -> println(message) },
        warning = { message -> println(message) },
        fine = { message: String -> println(message) },
    )
}