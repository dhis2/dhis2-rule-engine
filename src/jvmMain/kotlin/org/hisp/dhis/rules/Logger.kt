package org.hisp.dhis.rules

actual fun createLogger(className: String): Logger{
    val javaLogger = org.slf4j.LoggerFactory.getLogger(className)
    return Logger({message -> javaLogger.error(message)}, {message: String -> javaLogger.debug(message)})
}