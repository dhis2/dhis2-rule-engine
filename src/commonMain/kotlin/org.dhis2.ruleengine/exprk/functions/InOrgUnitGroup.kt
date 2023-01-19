package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.RuleVariableValue
import org.dhis2.ruleengine.exprk.internal.Function

const val IN_ORG_UNIT_GROUP = "d2:inOrgUnitGroup"

class InOrgUnitGroup(
    private val valueMap: Map<String, RuleVariableValue>,
    private val supplementaryData: Map<String, List<String>>
) : Function() {
    override fun call(arguments: List<String?>): String {
        val orgUnitGroup = arguments.first()
        val result = when {
            !valueMap.containsKey("org_unit") || !supplementaryData.containsKey(orgUnitGroup) -> false
            else -> supplementaryData[orgUnitGroup]?.contains(valueMap["org_unit"]?.value ?: "")
        }
        return result.toString()
    }
}