package org.hisp.dhis.rules

actual fun createLogger(className: String): Logger =
    Logger(
        severe = { message -> console.error(message) },
        warning = { message -> console.warn(message) },
        fine = { message ->
            if (RuleEngineJs.verbose) console.info(message)
        },
    )
