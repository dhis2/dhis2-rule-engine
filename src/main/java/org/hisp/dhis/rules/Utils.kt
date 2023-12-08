package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleDataValue
import org.hisp.dhis.rules.models.RuleEvent
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.annotation.Nonnull

val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    const val VARIABLE_PATTERN = "[#]\\{([\\w -_.]+)\\}"
    val VARIABLE_PATTERN_COMPILED = Pattern.compile(VARIABLE_PATTERN)
    fun values(ruleDataValues: List<RuleDataValue>): List<String> {
        val values: MutableList<String> = ArrayList(ruleDataValues.size)
        for (ruleDataValue in ruleDataValues) {
            values.add(ruleDataValue.value)
        }
        return Collections.unmodifiableList(values)
    }

    fun getLastUpdateDateForPrevious(
        ruleDataValues: List<RuleDataValue>,
        ruleEvent: RuleEvent
    ): String {
        val dates: MutableList<Date> = ArrayList()
        for (date in ruleDataValues) {
            val d = date.eventDate
            if (d.before(ruleEvent.eventDate)) {
                dates.add(d)
            }
        }
        return dateFormat.format(Collections.max(dates))
    }

    fun getLastUpdateDate(ruleDataValues: List<RuleDataValue>): String {
        val dates: MutableList<Date> = ArrayList()
        for (date in ruleDataValues) {
            val d = date.eventDate
            dates.add(d)
        }
        return dateFormat.format(Collections.max(dates))
    }

    @Nonnull
    fun unwrapVariableName(@Nonnull variable: String): String {
        val variableNameMatcher = VARIABLE_PATTERN_COMPILED.matcher(variable)

        // extract variable name
        if (variableNameMatcher.find()) {
            return variableNameMatcher.group(1)
        }
        throw IllegalArgumentException("Malformed variable: $variable")
    }
