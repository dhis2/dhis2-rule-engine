package org.hisp.dhis.rules

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hisp.dhis.rules.models.RuleDataValue
import org.hisp.dhis.rules.models.RuleEvent
import java.util.regex.Pattern
import kotlin.collections.ArrayList

    const val VARIABLE_PATTERN = "[#]\\{([\\w -_.]+)\\}"
    val VARIABLE_PATTERN_COMPILED = Pattern.compile(VARIABLE_PATTERN)
    fun values(ruleDataValues: List<RuleDataValue>): List<String> {
        val values: MutableList<String> = ArrayList(ruleDataValues.size)
        for (ruleDataValue in ruleDataValues) {
            values.add(ruleDataValue.value)
        }
        return values
    }

    fun getLastUpdateDateForPrevious(
        ruleDataValues: List<RuleDataValue>,
        ruleEvent: RuleEvent
    ): String {
        val dates: MutableList<LocalDate> = ArrayList()
        for (date in ruleDataValues) {
            val d = date.eventDate
            if (d < ruleEvent.eventDate) {
                dates.add(d)
            }
        }
        return dates.max().toString()
    }

    fun getLastUpdateDate(ruleDataValues: List<RuleDataValue>): String {
        val dates: MutableList<LocalDate> = ArrayList()
        for (date in ruleDataValues) {
            val d = date.eventDate
            dates.add(d)
        }
        return dates.max().toString()
    }

    fun unwrapVariableName(variable: String): String {
        val variableNameMatcher = VARIABLE_PATTERN_COMPILED.matcher(variable)

        // extract variable name
        if (variableNameMatcher.find()) {
            return variableNameMatcher.group(1)
        }
        throw IllegalArgumentException("Malformed variable: $variable")
    }

    fun LocalDate.Companion.currentDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
