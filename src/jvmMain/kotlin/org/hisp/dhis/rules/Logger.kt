package org.hisp.dhis.rules

import org.slf4j.LoggerFactory

actual fun createLogger(className: String): Logger {
    val javaLogger = LoggerFactory.getLogger(className)
    return Logger(
        severe = { message -> javaLogger.error(message) },
        warning = { message -> javaLogger.warn(message) },
        fine = { message -> javaLogger.debug(message) },
    )
}