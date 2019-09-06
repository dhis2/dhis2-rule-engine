package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleEnrollment
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleVariable
import org.hisp.dhis.rules.models.TriggerEnvironment
import kotlin.jvm.JvmStatic

expect class RuleVariableValueMapBuilder() {

    private constructor(ruleEnrollment: RuleEnrollment)

    private constructor(ruleEvent: RuleEvent)

    fun ruleVariables(ruleVariables: List<RuleVariable>): RuleVariableValueMapBuilder

    fun ruleEnrollment(ruleEnrollment: RuleEnrollment?): RuleVariableValueMapBuilder

    fun triggerEnvironment(triggerEnvironment: TriggerEnvironment?): RuleVariableValueMapBuilder

    fun ruleEvents(ruleEvents: List<RuleEvent>): RuleVariableValueMapBuilder

    fun calculatedValueMap(calculatedValueMap: Map<String, Map<String, String>>): RuleVariableValueMapBuilder

    fun constantValueMap(constantValues: Map<String, String>): RuleVariableValueMapBuilder

    fun build(): Map<String, RuleVariableValue>



    companion object {

        @JvmStatic fun target(ruleEnrollment: RuleEnrollment): RuleVariableValueMapBuilder

        @JvmStatic fun target(ruleEvent: RuleEvent): RuleVariableValueMapBuilder

    }
}
