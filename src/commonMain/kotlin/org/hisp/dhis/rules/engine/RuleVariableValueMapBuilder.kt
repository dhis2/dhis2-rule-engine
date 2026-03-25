package org.hisp.dhis.rules.engine

import kotlinx.datetime.LocalDate
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
    ): Map<String, RuleVariableValue> {
        val allEvents =
            ruleEvent?.let {
                val set = HashSet(ruleEvents)
                set.add(it)
                set.toSet()
            } ?: ruleEvents
        val allEventValues = buildAllEventValues(allEvents)
        val currentEnrollmentValues = ruleEnrollment?.let { buildCurrentEnrollmentValues(it) }.orEmpty()
        return build(allConstantValues, ruleVariables, allEvents, ruleEnrollment, ruleEvent, allEventValues, currentEnrollmentValues)
    }

    fun multipleBuild(
        allConstantValues: Map<String, String>,
        ruleVariables: List<RuleVariable>,
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment? = null,
    ): RuleVariableValueMap {
        val allEventValues = buildAllEventValues(ruleEvents)
        val currentEnrollmentValues = ruleEnrollment?.let { buildCurrentEnrollmentValues(it) }.orEmpty()
        val enrollmentMap =
            ruleEnrollment
                ?.let { enrollment ->
                    mapOf(Pair(enrollment, build(allConstantValues, ruleVariables, ruleEvents, enrollment, null, allEventValues, currentEnrollmentValues)))
                }.orEmpty()
        return RuleVariableValueMap(
            enrollmentMap,
            ruleEvents.associateBy({
                it
            }, { event -> build(allConstantValues, ruleVariables, ruleEvents, ruleEnrollment, event, allEventValues, currentEnrollmentValues) }),
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
    ): Map<String, RuleVariableValue> {
        val valueMap = HashMap<String, RuleVariableValue>()
        fillEnvironmentVariables(allEvents, ruleEnrollment, ruleEvent, valueMap)
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
        return allEventsValues.toMap()
    }

    private fun fillConstantsValues(
        allConstantValues: Map<String, String>,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        allConstantValues.forEach { (key, value) ->
            valueMap[key] = RuleVariableValue(RuleValueType.NUMERIC, value)
        }
    }

    private fun fillEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment?,
        ruleEvent: RuleEvent?,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        val currentDate = currentDate()
        fillEnvironmentVariables(ruleEvents, currentDate, valueMap)
        ruleEnrollment?.let { fillEnrollmentEnvironmentVariables(it, currentDate, valueMap) }
        ruleEvent?.let { fillEventEnvironmentVariables(ruleEvents, it, currentDate, valueMap) }
    }

    private fun fillEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        currentDate: LocalDate,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        valueMap[RuleEngineUtils.ENV_VAR_CURRENT_DATE] =
            RuleVariableValue(
                RuleValueType.DATE,
                currentDate.toString(),
                listOf(currentDate.toString()),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_COUNT] =
            RuleVariableValue(
                RuleValueType.NUMERIC,
                ruleEvents.size.toString(),
                listOf(ruleEvents.size.toString()),
                currentDate.toString(),
            )
        val environment = TriggerEnvironment.SERVER.clientName
        valueMap[RuleEngineUtils.ENV_VAR_ENVIRONMENT] =
            RuleVariableValue(
                RuleValueType.TEXT,
                environment,
                listOf(environment),
                currentDate.toString(),
            )
    }

    private fun fillEventEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        ruleEvent: RuleEvent,
        currentDate: LocalDate,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        val eventDate =
            if (ruleEvent.eventDate < RuleLocalDate.distantFuture())
                ruleEvent.eventDate.toString()
            else null
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_DATE] =
            RuleVariableValue(
                RuleValueType.DATE,
                eventDate,
                eventDate?.let { listOf(it) } ?: emptyList(),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_DUE_DATE] =
            RuleVariableValue(
                RuleValueType.DATE,
                ruleEvent.dueDate?.toString(),
                ruleEvent.dueDate?.let { listOf(it.toString()) } ?: emptyList(),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_COMPLETED_DATE] =
            RuleVariableValue(
                RuleValueType.DATE,
                ruleEvent.completedDate?.toString(),
                ruleEvent.completedDate?.let { listOf(it.toString()) } ?: emptyList(),
                currentDate.toString(),
            )
        val eventCount = ruleEvents.size.toString()
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_COUNT] =
            RuleVariableValue(
                RuleValueType.NUMERIC,
                eventCount,
                listOf(eventCount),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_ID] =
            RuleVariableValue(
                RuleValueType.TEXT,
                ruleEvent.event,
                listOf(ruleEvent.event),
                ruleEvent.eventDate.toString(),
            )
        val status = ruleEvent.status.toString()
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_STATUS] =
            RuleVariableValue(
                RuleValueType.TEXT,
                status,
                listOf(status),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_OU] = RuleVariableValue(RuleValueType.TEXT, ruleEvent.organisationUnit)
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_ID] = RuleVariableValue(RuleValueType.TEXT, ruleEvent.programStage)
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_NAME] = RuleVariableValue(RuleValueType.TEXT, ruleEvent.programStageName)
        valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] = RuleVariableValue(RuleValueType.TEXT, ruleEvent.organisationUnitCode)
    }

    private fun fillEnrollmentEnvironmentVariables(
        ruleEnrollment: RuleEnrollment,
        currentDate: LocalDate,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_ID] =
            RuleVariableValue(
                RuleValueType.TEXT,
                ruleEnrollment.enrollment,
                listOf(ruleEnrollment.enrollment),
                ruleEnrollment.enrollmentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT] =
            RuleVariableValue(
                RuleValueType.NUMERIC,
                "1",
                listOf("1"),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_TEI_COUNT] =
            RuleVariableValue(
                RuleValueType.NUMERIC,
                "1",
                listOf("1"),
                currentDate.toString(),
            )
        val enrollmentDate = ruleEnrollment.enrollmentDate
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE] =
            RuleVariableValue(
                RuleValueType.DATE,
                enrollmentDate.toString(),
                listOf(enrollmentDate.toString()),
                currentDate.toString(),
            )
        val incidentDate = ruleEnrollment.incidentDate
        valueMap[RuleEngineUtils.ENV_VAR_INCIDENT_DATE] =
            RuleVariableValue(
                RuleValueType.DATE,
                incidentDate.toString(),
                listOf(incidentDate.toString()),
                currentDate.toString(),
            )
        val status = ruleEnrollment.status.toString()
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS] =
            RuleVariableValue(
                RuleValueType.TEXT,
                status,
                listOf(status),
                currentDate.toString(),
            )
        valueMap[RuleEngineUtils.ENV_VAR_OU] = RuleVariableValue(RuleValueType.TEXT, ruleEnrollment.organisationUnit)
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_NAME] = RuleVariableValue(RuleValueType.TEXT, ruleEnrollment.programName)
        valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] = RuleVariableValue(RuleValueType.TEXT, ruleEnrollment.organisationUnitCode)
    }

    private fun fillRuleVariableValues(
        ruleVariables: List<RuleVariable>,
        ruleEvent: RuleEvent?,
        allEventValues: Map<String, List<RuleDataValueHistory>>,
        currentEnrollmentValues: Map<String, RuleAttributeValue>,
        valueMap: MutableMap<String, RuleVariableValue>,
    ) {
        ruleVariables.forEach { variable ->
            valueMap[variable.name] = variable.createValues(ruleEvent, allEventValues, currentEnrollmentValues)
        }
    }
}
