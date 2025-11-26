package org.hisp.dhis.rules.models

import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsExport
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic
import kotlin.time.Clock
import kotlin.time.Instant

@JsExport
data class RuleInstant(val millis: Long) {
    private val instant: Instant = Instant.fromEpochMilliseconds(millis)

    internal fun toInstant(): Instant {
        return instant
    }

    override fun toString() = instant.toString()

    @OptIn(ExperimentalJsStatic::class)
    companion object{
        @JvmStatic
        @JsStatic
        fun now(): RuleInstant {
            return fromInstant(Clock.System.now())
        }

        @JsStatic
        fun parse(instant: String): RuleInstant {
            return fromInstant(Instant.parse(instant))
        }

        internal fun fromInstant(instant: Instant): RuleInstant {
            return RuleInstant(instant.toEpochMilliseconds())
        }
    }
}
