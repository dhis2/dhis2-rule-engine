package org.dhis2.ruleengine

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.dhis2.ruleengine.models.*
import org.junit.jupiter.api.Test

internal class RuleEngineTest {
    @Test
    fun testExpression() {
        val ruleEngine = RuleEngineTestUtils.getRuleEngine(
            rules = listOf(
                Rule(
                    name = "testing",
                    programStage = null,
                    priority = 0,
                    condition = "d2:hasValue(A{variable_test})",
                    actions = listOf(
                        RuleAction.Assign(
                            field = "A{tea_assign_uid}",
                            data = "A{variable_test} * 2",
                            content = "A{variable_test} * 3"
                        )
                    ),
                    uid = "uid_test"
                )
            ),
            ruleVariables = listOf(
                RuleVariable.RuleVariableAttribute(
                    name = "variable_test",
                    trackedEntityAttribute = "tea_uid",
                    trackedEntityAttributeType = RuleValueType.NUMERIC
                ),
                RuleVariable.RuleVariableAttribute(
                    name = "tea_assign_uid",
                    trackedEntityAttribute = "tea_uid",
                    trackedEntityAttributeType = RuleValueType.NUMERIC
                )
            ),
            ruleEnrollment = RuleEnrollment(
                enrollment = "enrollment_uid",
                programName = "program",
                incidentDate = LocalDate(2022,Month.JANUARY,1),
                enrollmentDate = LocalDate(2022,Month.JANUARY,1),
                status = RuleEnrollment.Status.ACTIVE,
                organisationUnit = "org_uid",
                organisationUnitCode = "org_code",
                attributeValues = listOf(
                    RuleAttributeValue(
                        trackedEntityAttribute = "tea_uid",
                        value = "11"
                    )
                )
            ),
            ruleEvents = emptyList()
        )
        val result = ruleEngine.evaluate(
            ruleEvent = null,
            ruleEnrollment = RuleEnrollment(
                enrollment = "enrollment_uid",
                programName = "program",
                incidentDate = LocalDate(2022,Month.JANUARY,1),
                enrollmentDate = LocalDate(2022,Month.JANUARY,1),
                status = RuleEnrollment.Status.ACTIVE,
                organisationUnit = "org_uid",
                organisationUnitCode = "org_code",
                attributeValues = listOf(
                    RuleAttributeValue(
                        trackedEntityAttribute = "tea_uid",
                        value = "11"
                    )
                )
            )
        )

        result.apply {
            assert(isNotEmpty())
            assert(first().ruleAction is RuleAction.Assign)
            assert(first().data == "22")
        }
    }
}