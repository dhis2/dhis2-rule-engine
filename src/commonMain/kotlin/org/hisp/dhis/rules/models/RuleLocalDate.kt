package org.hisp.dhis.rules.models

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsExport
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

@JsExport
data class RuleLocalDate(val year: Int, val month: Int, val day: Int) {
    private val localDate: LocalDate = LocalDate(year, month, day);

    internal fun toLocalDate(): LocalDate {
        return localDate;
    }

    override fun toString() = localDate.toString()

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

        internal fun fromLocalDate(localDate: LocalDate): RuleLocalDate {
            return RuleLocalDate(localDate.year, localDate.month.number, localDate.day)
        }
    }
}
