package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsExport
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic
import kotlin.time.Instant

@JsExport
data class RuleLocalDate(val year: Int, val month: Int, val day: Int): Comparable<RuleLocalDate> {
    internal val localDate: LocalDate = LocalDate(year, month, day);

    override fun toString() = localDate.toString()

    override fun compareTo(other: RuleLocalDate): Int {
        return localDate.compareTo(other.localDate)
    }

    @OptIn(ExperimentalJsStatic::class)
    companion object{
        @JvmStatic
        @JsStatic
        fun currentDate(): RuleLocalDate {
            return fromLocalDate(org.hisp.dhis.rules.utils.currentDate())
        }

        @JsStatic
        fun parse(dateString: String): RuleLocalDate {
            return fromLocalDate(LocalDate.parse(dateString))
        }

        @JvmStatic
        @JsStatic
        fun distantFuture(): RuleLocalDate {
            return fromLocalDate(Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.currentSystemDefault()).date)
        }

        internal fun fromLocalDate(localDate: LocalDate): RuleLocalDate {
            return RuleLocalDate(localDate.year, localDate.month.number, localDate.day)
        }
    }
}
