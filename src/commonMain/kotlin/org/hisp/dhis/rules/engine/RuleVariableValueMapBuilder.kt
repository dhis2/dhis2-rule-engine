package org.hisp.dhis.rules.engine

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hisp.dhis.rules.api.RuleEngine
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
        return buildEnvironmentVariables(allEvents, ruleEnrollment, ruleEvent) +
            buildRuleVariableValues(ruleVariables, allEvents, ruleEnrollment, ruleEvent) +
            buildConstantsValues(allConstantValues)
    }

    fun multipleBuild(
        allConstantValues: Map<String, String>,
        ruleVariables: List<RuleVariable>,
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment? = null,
    ): RuleVariableValueMap {
        val enrollmentMap =
            ruleEnrollment
                ?.let { enrollment ->
                    mapOf(Pair(enrollment, build(allConstantValues, ruleVariables, ruleEvents, ruleEnrollment)))
                }.orEmpty()
        return RuleVariableValueMap(
            enrollmentMap,
            ruleEvents.associateBy({
                it
            }, { build(allConstantValues, ruleVariables, ruleEvents, ruleEnrollment, it) }),
        )
    }

    private fun buildCurrentEnrollmentValues(ruleEnrollment: RuleEnrollment): Map<String, RuleAttributeValue> =
        ruleEnrollment.attributeValues.associateBy {
            it.trackedEntityAttribute
        }

    private fun buildAllEventValues(ruleEvents: Set<RuleEvent>): Map<String, List<RuleDataValueHistory>> {
        val allEventsValues: MutableMap<String, MutableList<RuleDataValueHistory>> = HashMap()
        val events: MutableList<RuleEvent> = ArrayList(ruleEvents)

        // sort list of events by eventDate and createdDate:
        events.sortWith(compareBy<RuleEvent>({ it.eventDate }, { it.createdDate }).reversed())

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
                    RuleDataValueHistory(ruleDataValue.value, events[i].eventDate, events[i].createdDate, events[i].programStage),
                )
            }
        }
        return allEventsValues.toMap()
    }

    private fun buildConstantsValues(allConstantValues: Map<String, String>): Map<String, RuleVariableValue> =
        allConstantValues.mapValues {
            RuleVariableValue(RuleValueType.NUMERIC, it.value)
        }

    private fun buildEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment?,
        ruleEvent: RuleEvent?,
    ): Map<String, RuleVariableValue> {
        val currentDate = LocalDate.Companion.currentDate()

        val environmentVariablesValuesMap = buildEnvironmentVariables(ruleEvents, currentDate)
        val enrollmentVariableValueMap = ruleEnrollment?.let { buildEnrollmentEnvironmentVariables(it, currentDate) }.orEmpty()
        val eventVariableValueMap = ruleEvent?.let { buildEventEnvironmentVariables(ruleEvents, it, currentDate) }.orEmpty()

        return environmentVariablesValuesMap + enrollmentVariableValueMap + eventVariableValueMap
    }

    private fun buildEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        currentDate: LocalDate,
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        valueMap[RuleEngineUtils.ENV_VAR_CURRENT_DATE] =
            RuleVariableValue(
                RuleValueType.TEXT,
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

        return valueMap.toMap()
    }

    private fun buildEventEnvironmentVariables(
        ruleEvents: Set<RuleEvent>,
        ruleEvent: RuleEvent,
        currentDate: LocalDate,
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        if (ruleEvent.eventDate < Instant.DISTANT_FUTURE ){
            val eventDate =
                ruleEvent.eventDate
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
                    .toString()
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_DATE] =
                RuleVariableValue(
                    RuleValueType.TEXT,
                    eventDate,
                    listOf(eventDate),
                    currentDate.toString(),
                )
        } else {
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_DATE] =
                RuleVariableValue(
                    RuleValueType.TEXT,
                    null,
                    listOf(),
                    currentDate.toString(),
                );
        }
        if (ruleEvent.dueDate != null) {
            val dueDate = ruleEvent.dueDate
            valueMap[RuleEngineUtils.ENV_VAR_DUE_DATE] =
                RuleVariableValue(
                    RuleValueType.TEXT,
                    dueDate.toString(),
                    listOf(dueDate.toString()),
                    currentDate.toString(),
                )
        }
        if (ruleEvent.completedDate != null) {
            val completedDate = ruleEvent.completedDate
            valueMap[RuleEngineUtils.ENV_VAR_COMPLETED_DATE] =
                RuleVariableValue(
                    RuleValueType.TEXT,
                    completedDate.toString(),
                    listOf(completedDate.toString()),
                    currentDate.toString(),
                )
        }

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
                currentDate.toString(),
            )
        val status = ruleEvent.status.toString()
        valueMap[RuleEngineUtils.ENV_VAR_EVENT_STATUS] =
            RuleVariableValue(
                RuleValueType.TEXT,
                status,
                listOf(status),
                currentDate.toString(),
            )
        val organisationUnit = ruleEvent.organisationUnit
        valueMap[RuleEngineUtils.ENV_VAR_OU] = RuleVariableValue(RuleValueType.TEXT, organisationUnit)
        val programStageId = ruleEvent.programStage
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_ID] = RuleVariableValue(RuleValueType.TEXT, programStageId)
        val programStageName = ruleEvent.programStageName
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_NAME] = RuleVariableValue(RuleValueType.TEXT, programStageName)
        val organisationUnitCode = ruleEvent.organisationUnitCode
        valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] =
            RuleVariableValue(RuleValueType.TEXT, organisationUnitCode)

        return valueMap.toMap()
    }

    private fun buildEnrollmentEnvironmentVariables(
        ruleEnrollment: RuleEnrollment,
        currentDate: LocalDate,
    ): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_ID] =
            RuleVariableValue(
                RuleValueType.TEXT,
                ruleEnrollment.enrollment,
                listOf(ruleEnrollment.enrollment),
                currentDate.toString(),
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
                RuleValueType.TEXT,
                enrollmentDate.toString(),
                listOf(enrollmentDate.toString()),
                currentDate.toString(),
            )
        val incidentDate = ruleEnrollment.incidentDate
        valueMap[RuleEngineUtils.ENV_VAR_INCIDENT_DATE] =
            RuleVariableValue(
                RuleValueType.TEXT,
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
        val organisationUnit = ruleEnrollment.organisationUnit
        valueMap[RuleEngineUtils.ENV_VAR_OU] = RuleVariableValue(RuleValueType.TEXT, organisationUnit)
        val programName = ruleEnrollment.programName
        valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_NAME] = RuleVariableValue(RuleValueType.TEXT, programName)
        val organisationUnitCode = ruleEnrollment.organisationUnitCode
        valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] =
            RuleVariableValue(RuleValueType.TEXT, organisationUnitCode)

        return valueMap.toMap()
    }

    private fun buildRuleVariableValues(
        ruleVariables: List<RuleVariable>,
        ruleEvents: Set<RuleEvent>,
        ruleEnrollment: RuleEnrollment?,
        ruleEvent: RuleEvent?,
    ): Map<String, RuleVariableValue> {
        // map data values within all events to data elements
        val allEventValues = buildAllEventValues(ruleEvents)

        // map tracked entity attributes to values from enrollment
        val currentEnrollmentValues = ruleEnrollment?.let { buildCurrentEnrollmentValues(it) }.orEmpty()

        return ruleVariables.associateBy(
            { it.name },
            {
                it.createValues(
                    ruleEvent,
                    allEventValues,
                    currentEnrollmentValues,
                )
            },
        )
    }
}
