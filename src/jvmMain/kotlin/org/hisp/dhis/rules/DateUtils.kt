package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleInstant
import org.hisp.dhis.rules.models.RuleLocalDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class DateUtils {
    companion object {
        @JvmStatic
        fun toRuleInstant(instant: Instant): RuleInstant {
            return RuleInstant(instant.toEpochMilli())
        }

        @JvmStatic
        fun toRuleInstant(date: Date): RuleInstant {
            return toRuleInstant(date.toInstant())
        }

        @JvmStatic
        fun toRuleLocalDate(localDate: LocalDate): RuleLocalDate {
            return RuleLocalDate(localDate.year, localDate.monthValue, localDate.dayOfMonth)
        }

        @JvmStatic
        fun toRuleLocalDate(date: Date): RuleLocalDate {
            return toRuleLocalDate(date.toInstant())
        }

        @JvmStatic
        fun toRuleLocalDate(instant: Instant): RuleLocalDate {
            val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            return toRuleLocalDate(localDate)
        }
    }
}
