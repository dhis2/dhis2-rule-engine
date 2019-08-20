package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleDataValue

internal class Utils {

    companion object {
        fun values(ruleDataValues: List<RuleDataValue>): List<String?> {
            return ruleDataValues.map { it: RuleDataValue -> it.value }
        }
    }
}
