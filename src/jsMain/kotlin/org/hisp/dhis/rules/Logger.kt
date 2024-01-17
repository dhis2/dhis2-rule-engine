package org.hisp.dhis.rules

import org.hisp.dhis.lib.expression.js.ExpressionJs

actual fun createLogger(className: String): Logger {
    return Logger({message -> println(message)}, {message: String -> println(message)})
}