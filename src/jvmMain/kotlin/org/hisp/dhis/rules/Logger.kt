package org.hisp.dhis.rules

actual fun createLogger(className: String): Logger{
    val javaLogger = java.util.logging.Logger.getLogger(className)
    return Logger({message -> javaLogger.severe(message)}, {message: String -> javaLogger.fine(message)})
}