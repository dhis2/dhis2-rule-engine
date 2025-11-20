package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

@JsExport
data class RuleLocalDate(val year: Int, val month: Int, val day: Int) {
    private val localDate: LocalDate = LocalDate(year, month, day);

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
            return RuleLocalDate(localDate.year, localDate.month.number, localDate.day)
        }
    }
}
