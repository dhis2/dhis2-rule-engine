package org.hisp.dhis.rules

import org.slf4j.LoggerFactory

actual fun createLogger(className: String): Logger{
    val javaLogger = LoggerFactory.getLogger(className)
    return Logger({message -> javaLogger.error(message)}, {message: String -> javaLogger.debug(message)})
}