package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

@JsExport
data class RuleLocalDate(val year: Int, val month: Int, val day: Int) {
    private val localDate: LocalDate = LocalDate.parse("$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}");

    internal fun toLocalDate(): LocalDate {
        return localDate;
    }

    override fun toString() = localDate.toString()

    companion object{
        @JvmStatic
        fun currentDate(): RuleLocalDate {
            return fromLocalDate(org.hisp.dhis.rules.utils.currentDate())
        }

        internal fun parse(dateString: String): RuleLocalDate {
            return fromLocalDate(LocalDate.parse(dateString))
        }

        internal fun fromLocalDate(localDate: LocalDate): RuleLocalDate {
            return RuleLocalDate(localDate.year, localDate.month.ordinal + 1, localDate.day)
        }
    }
}
