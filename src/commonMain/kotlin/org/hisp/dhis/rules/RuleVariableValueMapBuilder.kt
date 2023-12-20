package org.hisp.dhis.rules

import kotlinx.datetime.LocalDate
import org.hisp.dhis.rules.models.*
import org.hisp.dhis.rules.utils.RuleEngineUtils
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RuleVariableValueMapBuilder private constructor() {
    private val allConstantValues: MutableMap<String, String>

    private val ruleVariables: MutableList<RuleVariable>

    private val ruleEvents: MutableList<RuleEvent>
    var ruleEnrollment: org.hisp.dhis.rules.models.RuleEnrollment? = null
    var ruleEvent: RuleEvent? = null
    private var triggerEnvironment: TriggerEnvironment? = null

    init {
        // collections used for construction of resulting variable value map
        ruleVariables = ArrayList()
        ruleEvents = ArrayList()
        allConstantValues = HashMap()
    }

    private constructor(ruleEnrollment: org.hisp.dhis.rules.models.RuleEnrollment) : this() {

        // enrollment is the target
        this.ruleEnrollment = ruleEnrollment
    }

    private constructor(ruleEvent: RuleEvent) : this() {

        // event is the target
        this.ruleEvent = ruleEvent
    }
    
    fun ruleVariables(ruleVariables: List<RuleVariable>?): RuleVariableValueMapBuilder {
        this.ruleVariables.addAll(ruleVariables!!)
        return this
    }

    fun ruleEnrollment(ruleEnrollment: org.hisp.dhis.rules.models.RuleEnrollment?): RuleVariableValueMapBuilder {
        check(this.ruleEnrollment == null) {
            "It seems that enrollment has been set as target " +
                    "already. It can't be used as a part of execution context."
        }
        this.ruleEnrollment = ruleEnrollment
        return this
    }

    fun triggerEnvironment(triggerEnvironment: TriggerEnvironment?): RuleVariableValueMapBuilder {
        check(this.triggerEnvironment == null) { "triggerEnvironment == null" }
        this.triggerEnvironment = triggerEnvironment
        return this
    }

    fun ruleEvents(ruleEvents: List<RuleEvent>): RuleVariableValueMapBuilder {
        check(!isEventInList(ruleEvents, ruleEvent)) {
                "ruleEvent %s is already set " +
                        "as a target, but also present in the context: events list ${ruleEvent!!.event}"
        }
        this.ruleEvents.addAll(ruleEvents)
        return this
    }

    fun constantValueMap(constantValues: Map<String, String>?): RuleVariableValueMapBuilder {
        allConstantValues.putAll(constantValues!!)
        return this
    }

    fun build(): MutableMap<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()

        // set environment variables
        valueMap.putAll(buildEnvironmentVariables())

        // set metadata variables
        valueMap.putAll(buildRuleVariableValues())

        // set constants value map
        valueMap.putAll(buildConstantsValues())

        return valueMap
    }

    fun multipleBuild(): RuleVariableValueMap {
        val enrollmentMap: MutableMap<RuleEnrollment, MutableMap<String, RuleVariableValue>> = HashMap()
        if (ruleEnrollment != null) {
            enrollmentMap[ruleEnrollment!!] = build()
        }
        val eventMap: MutableMap<RuleEvent, MutableMap<String, RuleVariableValue>> = HashMap()
        for (event in ruleEvents) {
            ruleEvent = event
            eventMap[event] = build()
        }
        return RuleVariableValueMap(enrollmentMap, eventMap)
    }

    private fun isEventInList(
        ruleEvents: List<RuleEvent>,
        ruleEvent: RuleEvent?
    ): Boolean {
        if (ruleEvent != null) {
            for ((event1) in ruleEvents) {
                if (event1 == ruleEvent.event) {
                    return true
                }
            }
        }
        return false
    }

    private fun buildCurrentEventValues(): Map<String, org.hisp.dhis.rules.models.RuleDataValue> {
        val currentEventValues: MutableMap<String, org.hisp.dhis.rules.models.RuleDataValue> = HashMap()
        if (ruleEvent != null) {
            for (index in ruleEvent!!.dataValues.indices) {
                val ruleDataValue = ruleEvent!!.dataValues[index]
                currentEventValues[ruleDataValue.dataElement] = ruleDataValue
            }
        }
        return currentEventValues
    }

    private fun buildCurrentEnrollmentValues(): Map<String, org.hisp.dhis.rules.models.RuleAttributeValue> {
        val currentEnrollmentValues: MutableMap<String, org.hisp.dhis.rules.models.RuleAttributeValue> = HashMap()
        if (ruleEnrollment != null) {
            val ruleAttributeValues = ruleEnrollment!!.attributeValues
            for (attributeValue in ruleAttributeValues) {
                currentEnrollmentValues[attributeValue.trackedEntityAttribute] = attributeValue
            }
        }
        return currentEnrollmentValues
    }

    private fun buildAllEventValues(): Map<String, MutableList<org.hisp.dhis.rules.models.RuleDataValue>> {
        val allEventsValues: MutableMap<String, MutableList<org.hisp.dhis.rules.models.RuleDataValue>> = HashMap()
        val events: MutableList<RuleEvent> = ArrayList(ruleEvents)
        if (ruleEvent != null) {
            // target event should be among the list of all
            // events in order to achieve correct behavior
            events.add(ruleEvent!!)
        }

        // sort list of events by eventDate:
        events.sortByDescending { e -> e.eventDate }

        // aggregating values by data element uid
        for (i in events.indices) {
            val (_, _, _, _, _, _, _, _, _, dataValues) = events[i]
            for (j in dataValues.indices) {
                val ruleDataValue = dataValues[j]

                // push new list if it is not there for the given data element
                if (!allEventsValues.containsKey(ruleDataValue.dataElement)) {
                    allEventsValues[ruleDataValue.dataElement] = ArrayList(events.size) //NOPMD
                }

                // append data value to the list
                allEventsValues[ruleDataValue.dataElement]!!.add(ruleDataValue)
            }
        }
        return allEventsValues
    }

    private fun buildConstantsValues(): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        for ((key, value) in allConstantValues) {
            valueMap[key] = RuleVariableValue(RuleValueType.NUMERIC, value)
        }
        return valueMap
    }

    private fun buildEnvironmentVariables(): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()
        val currentDate = LocalDate.Companion.currentDate()
        valueMap[RuleEngineUtils.ENV_VAR_CURRENT_DATE] = RuleVariableValue(
            RuleValueType.TEXT,
            currentDate.toString(),
            listOf(currentDate.toString()),
            currentDate.toString()
        )
        if (triggerEnvironment != null) {
            val environment = triggerEnvironment!!.clientName
            valueMap[RuleEngineUtils.ENV_VAR_ENVIRONMENT] = RuleVariableValue(
                RuleValueType.TEXT,
                environment,
                listOf(environment),
                currentDate.toString()
            )
        }
        if (!ruleEvents.isEmpty()) {
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_COUNT] = RuleVariableValue(
                RuleValueType.NUMERIC, ruleEvents.size.toString(),
                listOf(ruleEvents.size.toString()), currentDate.toString()
            )
        }
        if (ruleEnrollment != null) {
            valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_ID] = RuleVariableValue(
                RuleValueType.TEXT, ruleEnrollment!!.enrollment,
                listOf(ruleEnrollment!!.enrollment), currentDate.toString()
            )
            valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT] = RuleVariableValue(
                RuleValueType.NUMERIC, "1",
                listOf("1"), currentDate.toString()
            )
            valueMap[RuleEngineUtils.ENV_VAR_TEI_COUNT] = RuleVariableValue(
                RuleValueType.NUMERIC, "1",
                listOf("1"), currentDate.toString()
            )
            val enrollmentDate = ruleEnrollment!!.enrollmentDate
            valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE] = RuleVariableValue(
                RuleValueType.TEXT, enrollmentDate.toString(),
                listOf(enrollmentDate.toString()), currentDate.toString()
            )
            val incidentDate = ruleEnrollment!!.incidentDate
            valueMap[RuleEngineUtils.ENV_VAR_INCIDENT_DATE] = RuleVariableValue(
                RuleValueType.TEXT, incidentDate.toString(),
                listOf(incidentDate.toString()), currentDate.toString()
            )
            val status = ruleEnrollment!!.status.toString()
            valueMap[RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS] = RuleVariableValue(
                RuleValueType.TEXT, status,
                listOf(status), currentDate.toString()
            )
            val organisationUnit = ruleEnrollment!!.organisationUnit
            valueMap[RuleEngineUtils.ENV_VAR_OU] = RuleVariableValue(RuleValueType.TEXT, organisationUnit)
            val programName = ruleEnrollment!!.programName
            valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_NAME] = RuleVariableValue(RuleValueType.TEXT, programName)
            val organisationUnitCode = ruleEnrollment!!.organisationUnitCode
            valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] =
                RuleVariableValue(RuleValueType.TEXT, organisationUnitCode)
        }
        if (ruleEvent != null) {
            val eventDate = ruleEvent!!.eventDate
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_DATE] = RuleVariableValue(
                RuleValueType.TEXT, eventDate.toString(),
                listOf(eventDate.toString()), currentDate.toString()
            )
            if (ruleEvent!!.dueDate != null) {
                val dueDate = ruleEvent!!.dueDate
                valueMap[RuleEngineUtils.ENV_VAR_DUE_DATE] = RuleVariableValue(
                    RuleValueType.TEXT, dueDate.toString(),
                    listOf(dueDate.toString()), currentDate.toString()
                )
            }
            if (ruleEvent!!.completedDate != null) {
                val completedDate = ruleEvent!!.completedDate
                valueMap[RuleEngineUtils.ENV_VAR_COMPLETED_DATE] = RuleVariableValue(
                    RuleValueType.TEXT, completedDate.toString(),
                    listOf(completedDate.toString()), currentDate.toString()
                )
            }

            // override value of event count
            var eventCount = (ruleEvents.size + 1).toString()
            if (ruleEvents.contains(ruleEvent)) {
                eventCount = ruleEvents.size.toString()
            }
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_COUNT] = RuleVariableValue(
                RuleValueType.NUMERIC, eventCount,
                listOf(eventCount), currentDate.toString()
            )
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_ID] = RuleVariableValue(
                RuleValueType.TEXT, ruleEvent!!.event,
                listOf(ruleEvent!!.event), currentDate.toString()
            )
            val status = ruleEvent!!.status.toString()
            valueMap[RuleEngineUtils.ENV_VAR_EVENT_STATUS] = RuleVariableValue(
                RuleValueType.TEXT, status, listOf(status), currentDate.toString()
            )
            val organisationUnit = ruleEvent!!.organisationUnit
            valueMap[RuleEngineUtils.ENV_VAR_OU] = RuleVariableValue(RuleValueType.TEXT, organisationUnit)
            val programStageId = ruleEvent!!.programStage
            valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_ID] = RuleVariableValue(RuleValueType.TEXT, programStageId )
            val programStageName = ruleEvent!!.programStageName
            valueMap[RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_NAME] = RuleVariableValue(RuleValueType.TEXT, programStageName)
            val organisationUnitCode = ruleEvent!!.organisationUnitCode
            valueMap[RuleEngineUtils.ENV_VAR_OU_CODE] =
                RuleVariableValue(RuleValueType.TEXT, organisationUnitCode)
        }
        return valueMap
    }

    private fun buildRuleVariableValues(): Map<String, RuleVariableValue> {
        val valueMap: MutableMap<String, RuleVariableValue> = HashMap()

        // map data values within all events to data elements
        val allEventValues = buildAllEventValues()

        // map tracked entity attributes to values from enrollment
        val currentEnrollmentValues = buildCurrentEnrollmentValues()

        // build a map of current event values
        val currentEventValues = buildCurrentEventValues()
        for (ruleVariable in ruleVariables) {
            valueMap.putAll(
                ruleVariable.createValues(this, allEventValues, currentEnrollmentValues, currentEventValues)
            )
        }
        return valueMap
    }

    companion object {
            fun target(ruleEnrollment: org.hisp.dhis.rules.models.RuleEnrollment): RuleVariableValueMapBuilder {
            return RuleVariableValueMapBuilder(ruleEnrollment)
        }

            fun target(ruleEvent: RuleEvent): RuleVariableValueMapBuilder {
            return RuleVariableValueMapBuilder(ruleEvent)
        }

            fun target(): RuleVariableValueMapBuilder {
            return RuleVariableValueMapBuilder()
        }
    }
}
