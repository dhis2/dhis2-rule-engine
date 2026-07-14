package org.hisp.dhis.rules

data class Logger(
    val severe: (String) -> Unit,
    val warning: (String) -> Unit,
    val fine: (String) -> Unit,
)

expect fun createLogger(className: String): Logger
