package org.hisp.dhis.rules.engine

import kotlinx.datetime.LocalDate
import org.hisp.dhis.lib.expression.spi.ValueType
import org.hisp.dhis.lib.expression.spi.VariableValue
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.RuleEngineUtils
import org.hisp.dhis.rules.utils.currentDate



internal class RuleVariableValueMapBuilder {
    fun build(
        allConstantValues: Map<String, String>,
        ruleVariables: List<RuleVariable>,
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment? = null,
        ruleEvent: RuleEvent? = null,
    ): MutableMap<String, VariableValue> {
        val allEvents =
            ruleEvent?.let {
                val set = HashSet(ruleEvents)
                set.add(it)
                set
            } ?: ruleEvents
        val allEventValues = buildAllEventValues(allEvents)
        val currentEnrollmentValues = ruleEnrollment?.let { buildCurrentEnrollmentValues(it) }.orEmpty()
        return build(allConstantValues, ruleVariables, allEvents, ruleEnrollment, ruleEvent, allEventValues, currentEnrollmentValues, currentDate())
    }

    fun multipleBuild(
        allConstantValues: Map<String, String>,
        ruleVariables: List<RuleVariable>,
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment? = null,
    ): RuleVariableValueMap {
        val allEventValues = buildAllEventValues(ruleEvents)
        val currentEnrollmentValues = ruleEnrollment?.let { buildCurrentEnrollmentValues(it) }.orEmpty()
        val currentDate = currentDate()
        val enrollmentMap =
            ruleEnrollment
                ?.let { enrollment ->
                    mapOf(Pair(enrollment, build(allConstantValues, ruleVariables, ruleEvents, enrollment, null, allEventValues, currentEnrollmentValues, currentDate)))
                }.orEmpty()
        return RuleVariableValueMap(
            enrollmentMap,
            ruleEvents.associateBy({
                it
            }, { event -> build(allConstantValues, ruleVariables, ruleEvents, ruleEnrollment, event, allEventValues, currentEnrollmentValues, currentDate) }),
        )
    }

    private fun build(
        allConstantValues: Map<String, String>,
        ruleVariables: List<RuleVariable>,
        allEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment?,
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        currentDate: LocalDate,
    ): MutableMap<String, VariableValue> {
        val valueMap = HashMap<String, VariableValue>()
        fillEnvironmentVariables(allEvents, ruleEnrollment, ruleEvent, currentDate, valueMap)
        fillRuleVariableValues(ruleVariables, ruleEvent, allEventValues, currentEnrollmentValues, valueMap)
        fillConstantsValues(allConstantValues, valueMap)
        return valueMap
    }

    private fun buildCurrentEnrollmentValues(ruleEnrollment: RuleEnrollment): Map<String, RuleAttributeValue> =
        ruleEnrollment.attributeValues.associateBy {
            it.trackedEntityAttribute
        }

    private fun buildAllEventValues(ruleEvents: Set<RuleEvent>): Map<String, List<RuleDataValueHistory>> {
        val allEventsValues: MutableMap<String, MutableList<RuleDataValueHistory>> = HashMap()
        val events: List<RuleEvent> = ruleEvents.sortedDescending()

        // aggregating values by data element uid
        for (i in events.indices) {
            val dataValues = events[i].dataValues
            for (j in dataValues.indices) {
                val ruleDataValue = dataValues[j]

                // push new list if it is not there for the given data element
                if (!allEventsValues.containsKey(ruleDataValue.dataElement)) {
                    allEventsValues[ruleDataValue.dataElement] = ArrayList(events.size)
                }

                // append data value to the list
                allEventsValues[ruleDataValue.dataElement]?.add(
                    RuleDataValueHistory(ruleDataValue.value, events[i].eventDate, events[i].resolvedCreatedDate, events[i].programStage),
                )
            }
        }
        return allEventsValues
    }

    private fun fillConstantsValues(
        allConstantValues: Map<String, String>,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        allConstantValues.forEach { (key, value) ->
            valueMap[key] = VariableValue(ValueType.NUMBER, value, listOf(), null)
        }
    }

    private fun fillEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment?,
        ruleEvent: RuleEvent?,
        currentDate: LocalDate,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        fillEnvironmentVariables(ruleEvents, currentDate, valueMap)
        ruleEnrollment?.let { fillEnrollmentEnvironmentVariables(it, currentDate, valueMap) }
        ruleEvent?.let { fillEventEnvironmentVariables(ruleEvents, it, currentDate, valueMap) }
    }

    private fun fillEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        currentDate: LocalDate,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        val currentDateStr = currentDate.toString()
        val eventCountStr = ruleEvents.size.toString()
        valueMap[RuleEngineUtils.ENV_VAR_CURRENT_DATE] =
            VariableValue(
                ValueType.DATE,
                currentDateStr,
                listOf(currentDateStr),
                currentDateStr,
            )
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_COUNT] =
            VariableValue(
                ValueType.NUMBER,
                eventCountStr,
                listOf(eventCountStr),
                currentDateStr,
            )
        val environment = TriggerEnvironment.SERVER.clientName
        valueMap[RuleEngineUtils.ENV_VAR_ENVIRONMENT] =
            VariableValue(
                ValueType.STRING,
                environment,
                listOf(environment),
                currentDateStr,
            )
    }

    private fun fillEventEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        ruleEvent: RuleEvent,
        currentDate: LocalDate,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        val currentDateStr = currentDate.toString()
        val eventDate =
            if (ruleEvent.eventDate < RuleLocalDate.distantFuture())
                ruleEvent.eventDate.toString()
            else null
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_DATE] =
            VariableValue(
                ValueType.DATE,
                eventDate,
                eventDate?.let { listOf(it) } ?: emptyList(),
                currentDateStr,
            )
        val dueDate = ruleEvent.dueDate?.toString()
        valueMap[RuleEngineUtils.ENV_VAR_DUE_DATE] =
            VariableValue(
                ValueType.DATE,
                dueDate,
                dueDate?.let { listOf(it) } ?: emptyList(),
                currentDateStr,
            )
        val completedDate = ruleEvent.completedDate?.toString()
        valueMap[RuleEngineUtils.ENV_VAR_COMPLETED_DATE] =
            VariableValue(
                ValueType.DATE,
                completedDate,
                completedDate?.let { listOf(it) } ?: emptyList(),
                currentDateStr,
            )
        val eventCountStr = ruleEvents.size.toString()
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_COUNT] =
            VariableValue(
                ValueType.NUMBER,
                eventCountStr,
                listOf(eventCountStr),
                currentDateStr,
            )
        val eventDateStr = ruleEvent.eventDate.toString()
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_ID] =
            VariableValue(
                ValueType.STRING,
                ruleEvent.event,
                listOf(ruleEvent.event),
                eventDateStr,
            )
        val status = ruleEvent.status.toString()
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_STATUS] =
            VariableValue(
                ValueType.STRING,
                status,
                listOf(status),
                currentDateStr,
            )
        valueMap[RuleEngineUtils.ENV_VAR_OU] = VariableValue(ValueType.STRING, ruleEvent.organisationUnit, listOf(), null)
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_ID] = VariableValue(ValueType.STRING, ruleEvent.programStage, listOf(), null)
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_NAME] = VariableValue(ValueType.STRING, ruleEvent.programStageName, listOf(), null)
        valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] = VariableValue(ValueType.STRING, ruleEvent.organisationUnitCode, listOf(), null)
    }

    private fun fillEnrollmentEnvironmentVariables(
        ruleEnrollment: RuleEnrollment,
        currentDate: LocalDate,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        val currentDateStr = currentDate.toString()
        val enrollmentDateStr = ruleEnrollment.enrollmentDate.toString()
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_ID] =
            VariableValue(
                ValueType.STRING,
                ruleEnrollment.enrollment,
                listOf(ruleEnrollment.enrollment),
                enrollmentDateStr,
            )
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT] =
            VariableValue(
                ValueType.NUMBER,
                "1",
                listOf("1"),
                currentDateStr,
            )
        valueMap[RuleEngineUtils.ENV_VAR_TEI_COUNT] =
            VariableValue(
                ValueType.NUMBER,
                "1",
                listOf("1"),
                currentDateStr,
            )
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE] =
            VariableValue(
                ValueType.DATE,
                enrollmentDateStr,
                listOf(enrollmentDateStr),
                currentDateStr,
            )
        val incidentDateStr = ruleEnrollment.incidentDate.toString()
        valueMap[RuleEngineUtils.ENV_VAR_INCIDENT_DATE] =
            VariableValue(
                ValueType.DATE,
                incidentDateStr,
                listOf(incidentDateStr),
                currentDateStr,
            )
        val status = ruleEnrollment.status.toString()
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS] =
            VariableValue(
                ValueType.STRING,
                status,
                listOf(status),
                currentDateStr,
            )
        valueMap[RuleEngineUtils.ENV_VAR_OU] = VariableValue(ValueType.STRING, ruleEnrollment.organisationUnit, listOf(), null)
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_NAME] = VariableValue(ValueType.STRING, ruleEnrollment.programName, listOf(), null)
        valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] = VariableValue(ValueType.STRING, ruleEnrollment.organisationUnitCode, listOf(), null)
    }

    private fun fillRuleVariableValues(
        ruleVariables: List<RuleVariable>,
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        valueMap: MutableMap<String, VariableValue>,
    ) {
        ruleVariables.forEach { variable ->
            valueMap[variable.name] = variable.createValues(ruleEvent, allEventValues, currentEnrollmentValues).toVariableValue()
        }
    }
}
