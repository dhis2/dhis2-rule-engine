package org.hisp.dhis.rules

import kotlinx.collections.immutable.toPersistentMap
import org.hisp.dhis.rules.RuleVariableValue.Companion.create
import org.hisp.dhis.rules.models.*
import java.text.SimpleDateFormat
import java.util.*

class RuleVariableValueMapBuilder() {

    private val dateFormat: SimpleDateFormat

    private val currentEventValues: MutableMap<String, RuleDataValue>

    private val currentEnrollmentValues: MutableMap<String, RuleAttributeValue>

    private val allEventsValues: MutableMap<String, MutableList<RuleDataValue>>

    private val calculatedValueMap: MutableMap<String, Map<String, String>>

    private val allConstantValues: MutableMap<String, String>

    private val ruleVariables: MutableList<RuleVariable>

    private val ruleEvents: MutableList<RuleEvent>

    private var ruleEnrollment: RuleEnrollment? = null

    private var ruleEvent: RuleEvent? = null

    private var triggerEnvironment: TriggerEnvironment? = null

    init {
        this.dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)

        // collections used for construction of resulting variable value map
        this.currentEnrollmentValues = hashMapOf()
        this.currentEventValues = hashMapOf()
        this.allEventsValues = hashMapOf()
        this.ruleVariables = arrayListOf()
        this.ruleEvents = arrayListOf()
        this.calculatedValueMap = hashMapOf()
        this.allConstantValues = hashMapOf()
    }

    private constructor(ruleEnrollment: RuleEnrollment) : this() {

        // enrollment is the target
        this.ruleEnrollment = ruleEnrollment
    }

    private constructor(ruleEvent: RuleEvent) : this() {

        // event is the target
        this.ruleEvent = ruleEvent
    }

    fun ruleVariables(ruleVariables: List<RuleVariable>) = apply { this.ruleVariables.addAll(ruleVariables) }

    fun ruleEnrollment(ruleEnrollment: RuleEnrollment?) =
            this.ruleEnrollment?.let { throw IllegalStateException("It seems that enrollment has been set as target already. " +
                    "It can't be used as a part of execution context.") } ?:
            apply { this.ruleEnrollment = ruleEnrollment }


    fun triggerEnvironment(triggerEnvironment: TriggerEnvironment?) =
            this.triggerEnvironment?.let { throw IllegalStateException("triggerEnvironment == null") } ?:
            apply { this.triggerEnvironment = triggerEnvironment }


    fun ruleEvents(ruleEvents: List<RuleEvent>): RuleVariableValueMapBuilder {
        if (isEventInList(ruleEvents, ruleEvent)) {
            throw IllegalStateException("ruleEvent ${ruleEvent!!.event} is already set as a target, " +
                    "but also present in the context: ruleEvents list")
        }

        this.ruleEvents.addAll(ruleEvents)
        return this
    }

    fun calculatedValueMap(calculatedValueMap: Map<String, Map<String, String>>) =
            apply { this.calculatedValueMap.putAll(calculatedValueMap) }

    fun constantValueMap(constantValues: Map<String, String>)=
            apply { this.allConstantValues.putAll(constantValues) }


    fun build(): Map<String, RuleVariableValue> {
        val valueMap = HashMap<String, RuleVariableValue>()

        // map tracked entity attributes to values from enrollment
        buildCurrentEnrollmentValues()

        // build a map of current event values
        buildCurrentEventValues()

        // map data values within all events to data elements
        buildAllEventValues()

        // set environment variables
        buildEnvironmentVariables(valueMap)

        // set metadata variables
        buildRuleVariableValues(valueMap)

        // set constants value map
        buildConstantsValues(valueMap)

        // do not let outer world to alter variable value map
        return valueMap.toPersistentMap()
    }

    private fun buildCurrentEventValues() {
        ruleEvent?.let { ruleEvent ->
            ruleEvent.dataValues?.forEach { dataValue -> currentEventValues[dataValue.dataElement!!] = dataValue }
        }
    }

    private fun buildCurrentEnrollmentValues() {
        ruleEnrollment?.let { ruleEnrollment ->
            ruleEnrollment.attributeValues?.forEach { attribute ->
                currentEnrollmentValues[attribute.trackedEntityAttribute!!] = attribute
            }
        }
    }

    private fun buildAllEventValues() {
        val events = ruleEvents

        // target event should be among the list of all
        // events in order to achieve correct behavior
        ruleEvent?.let { events.add(it) }

        // sort list of events by eventDate:
        events.sortWith(RuleEvent.EVENT_DATE_COMPARATOR)

        // aggregating values by data element uid
        events.forEach { event ->
            event.dataValues?.map { dataValue ->

                // push new list if it is not there for the given data element
                if(!allEventsValues.containsKey(dataValue.dataElement))
                    allEventsValues[dataValue.dataElement!!] = mutableListOf<RuleDataValue>()
                // append data value to the list

                allEventsValues[dataValue.dataElement]?.add(dataValue)

            }
        }
    }

    private fun buildConstantsValues(valueMap: MutableMap<String, RuleVariableValue>) {
        allConstantValues.forEach { (key, value) ->
            valueMap[key] = create(value, RuleValueType.NUMERIC)
        }
    }

    private fun buildEnvironmentVariables(valueMap: MutableMap<String, RuleVariableValue>) {
        val currentDate = dateFormat.format(Date())
        valueMap[ENV_VAR_CURRENT_DATE] = create(currentDate, RuleValueType.TEXT, listOf(currentDate), currentDate)

        triggerEnvironment?.let {
            valueMap[ENV_VAR_ENVIRONMENT] = create(it.clientName, RuleValueType.TEXT, listOf(it.clientName), currentDate)
        }

        if (ruleEvents.isNotEmpty()) {
            valueMap[ENV_VAR_EVENT_COUNT] = create(ruleEvents.size.toString(),
                    RuleValueType.NUMERIC, listOf(ruleEvents.size.toString()), currentDate)
        }

        ruleEnrollment?.let { ruleEnrollment ->
            with(ruleEnrollment) {

                valueMap[ENV_VAR_ENROLLMENT_ID] = create(enrollment, RuleValueType.TEXT, listOf(enrollment!!), currentDate)
                valueMap[ENV_VAR_ENROLLMENT_COUNT] = create("1", RuleValueType.NUMERIC, listOf("1"), currentDate)
                valueMap[ENV_VAR_TEI_COUNT] = create("1", RuleValueType.NUMERIC, listOf("1"), currentDate)

                val enrollmentDate = dateFormat.format(enrollmentDate)
                valueMap[ENV_VAR_ENROLLMENT_DATE] = create(enrollmentDate, RuleValueType.TEXT, listOf(enrollmentDate), currentDate)

                val incidentDate = dateFormat.format(incidentDate)
                valueMap[ENV_VAR_INCIDENT_DATE] = create(incidentDate, RuleValueType.TEXT, listOf(incidentDate), currentDate)

                val status = status!!.toString()
                valueMap[ENV_VAR_ENROLLMENT_STATUS] = create(status, RuleValueType.TEXT, listOf(status), currentDate)

                val organisationUnit = organisationUnit
                valueMap[ENV_VAR_OU] = create(organisationUnit, RuleValueType.TEXT)

                val programName = programName
                valueMap[ENV_VAR_PROGRAM_NAME] = create(programName, RuleValueType.TEXT)

                val organisationUnitCode = organisationUnitCode
                valueMap[ENV_VAR_OU_CODE] = create(organisationUnitCode, RuleValueType.TEXT)
            }

        }

        ruleEvent?.let { ruleEvent ->
            with(ruleEvent) {
                val eventDate = dateFormat.format(eventDate)
                valueMap[ENV_VAR_EVENT_DATE] = create(eventDate, RuleValueType.TEXT, listOf(eventDate), currentDate)

                val dueDate = dateFormat.format(dueDate)
                valueMap[ENV_VAR_DUE_DATE] = create(dueDate, RuleValueType.TEXT, listOf(dueDate), currentDate)

                // override value of event count
                val eventCount = (ruleEvents.size).toString()
                valueMap[ENV_VAR_EVENT_COUNT] = create(eventCount, RuleValueType.NUMERIC, listOf(eventCount), currentDate)
                valueMap[ENV_VAR_EVENT_ID] = create(event, RuleValueType.TEXT, listOf(event!!), currentDate)

                val status = status!!.toString()
                valueMap[ENV_VAR_EVENT_STATUS] = create(status, RuleValueType.TEXT, listOf(status), currentDate)

                val organisationUnit = organisationUnit
                valueMap[ENV_VAR_OU] = create(organisationUnit, RuleValueType.TEXT)

                val programStageId = programStage
                valueMap[ENV_VAR_PROGRAM_STAGE_ID] = create(programStageId, RuleValueType.TEXT)

                val programStageName = programStageName
                valueMap[ENV_VAR_PROGRAM_STAGE_NAME] = create(programStageName, RuleValueType.TEXT)

                val organisationUnitCode = organisationUnitCode
                valueMap[ENV_VAR_OU_CODE] = create(organisationUnitCode, RuleValueType.TEXT)
            }
        }
    }

    private fun buildRuleVariableValues(valueMap: MutableMap<String, RuleVariableValue>) {
        ruleVariables.forEach {
            when (it) {
                is RuleVariableAttribute -> createAttributeVariableValue(valueMap, it)
                is RuleVariableCurrentEvent -> createCurrentEventVariableValue(valueMap, it)
                is RuleVariablePreviousEvent -> createPreviousEventVariableValue(valueMap, it)
                is RuleVariableNewestEvent -> createNewestEventVariableValue(valueMap, it)
                is RuleVariableNewestStageEvent -> createNewestStageEventVariableValue(valueMap, it)
                is RuleVariableCalculatedValue -> createCalculatedValueVariable(valueMap, it)
                else -> throw IllegalArgumentException("Unsupported RuleVariable type: " + it.javaClass)
            }
        }
    }

    private fun createAttributeVariableValue(valueMap: MutableMap<String, RuleVariableValue>,
                                             variable: RuleVariableAttribute) {
        ruleEnrollment?.let {
            val currentDate = dateFormat.format(Date())

            val variableValue = if (currentEnrollmentValues.containsKey(variable.trackedEntityAttribute)) {
                val value = currentEnrollmentValues[variable.trackedEntityAttribute]
                create(value?.value, variable.trackedEntityAttributeType, listOf(value?.value!!), currentDate)
            } else {
                create(variable.trackedEntityAttributeType)
            }

            valueMap[variable.name!!] = variableValue
        } ?: return
    }

    private fun createCurrentEventVariableValue(valueMap: MutableMap<String, RuleVariableValue>,
                                                variable: RuleVariableCurrentEvent) {
        ruleEvent?.let {

            val variableValue = if (currentEventValues.containsKey(variable.dataElement)) {
                val value = currentEventValues[variable.dataElement]
                create(value?.value, variable.dataElementType, listOf(value?.value!!), getLastUpdateDate(listOf(value)))
            } else {
                create(variable.dataElementType)
            }

            valueMap[variable.name!!] = variableValue
        } ?: return
    }

    private fun createPreviousEventVariableValue(valueMap: MutableMap<String, RuleVariableValue>,
                                                 variable: RuleVariablePreviousEvent) {
        ruleEvent?.let { _ ->
            var variableValue: RuleVariableValue?
            val ruleDataValues = allEventsValues[variable.dataElement]

            ruleDataValues?.filter { ruleEvent?.eventDate!! > it.eventDate!! }
                    ?.first { dataValue -> ruleEvent?.eventDate!! > dataValue.eventDate!! }
                    .let { ruleDataValue ->
                        variableValue = create(ruleDataValue?.value, variable.dataElementType,
                            Utils.values(ruleDataValues!!), getLastUpdateDateForPrevious(ruleDataValues))
                    }

            valueMap[variable.name!!] = variableValue ?: create(variable.dataElementType)

        } ?: return
    }

    private fun createNewestEventVariableValue(valueMap: MutableMap<String, RuleVariableValue>,
                                               variable: RuleVariableNewestEvent) {

        val ruleDataValues = allEventsValues[variable.dataElement]

        if(ruleDataValues.isNullOrEmpty())
            valueMap[variable.name!!] = create(variable.dataElementType)
        else
            valueMap[variable.name!!] = create(ruleDataValues[0].value, variable.dataElementType,
                    Utils.values(ruleDataValues), getLastUpdateDate(ruleDataValues))
    }

    private fun getLastUpdateDate(ruleDataValues: List<RuleDataValue>): String {
        val dates = mutableListOf<Date>()

        ruleDataValues.forEach {
            if(!it.eventDate!!.after(Date()))
                dates.add(it.eventDate)
        }

        return dateFormat.format(dates.max())
    }

    private fun getLastUpdateDateForPrevious(ruleDataValues: List<RuleDataValue>): String {
        val dates = mutableListOf<Date>()

        ruleDataValues.forEach {
            if(!it.eventDate!!.after(Date()) && it.eventDate.before(ruleEvent!!.eventDate!!))
                dates.add(it.eventDate)
        }

        return dateFormat.format(dates.max())
    }

    private fun createNewestStageEventVariableValue(valueMap: MutableMap<String, RuleVariableValue>,
                                                    variable: RuleVariableNewestStageEvent) {

        val stageRuleDataValues = mutableListOf<RuleDataValue>()
        val sourceRuleDataValues = allEventsValues[variable.dataElement]

        if(!sourceRuleDataValues.isNullOrEmpty()) {
            // filter data values based on program stage
            sourceRuleDataValues
                    .filter { dataValue -> variable.programStage == dataValue.programStage }
                    .map { dataValue -> stageRuleDataValues.add(dataValue) }
        }

        if (stageRuleDataValues.isEmpty()) {
            valueMap[variable.name!!] = create(variable.dataElementType)
        } else {
            valueMap[variable.name!!] = create(stageRuleDataValues[0].value, variable.dataElementType,
                    Utils.values(stageRuleDataValues), getLastUpdateDate(stageRuleDataValues))
        }
    }

    private fun createCalculatedValueVariable(valueMap: MutableMap<String, RuleVariableValue>,
                                              variable: RuleVariableCalculatedValue) {
        ruleEnrollment?.let {

            val variableValue = if (calculatedValueMap.containsKey(it.enrollment)) {
                if (calculatedValueMap[it.enrollment]?.containsKey(variable.name!!) == true) {
                    val value = calculatedValueMap[it.enrollment]?.get(variable.name)
                    create(value, variable.calculatedValueType, listOf(value!!), dateFormat.format(Date()))
                } else {
                    create(variable.calculatedValueType)
                }
            } else {
                create(variable.calculatedValueType)
            }

            valueMap[variable.name!!] = variableValue
        } ?: return
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"

        private const val ENV_VAR_CURRENT_DATE = "current_date"

        private const val ENV_VAR_EVENT_DATE = "event_date"

        private const val ENV_VAR_EVENT_COUNT = "event_count"

        private const val ENV_VAR_DUE_DATE = "due_date"

        private const val ENV_VAR_EVENT_ID = "event_id"

        private const val ENV_VAR_ENROLLMENT_DATE = "enrollment_date"

        private const val ENV_VAR_ENROLLMENT_ID = "enrollment_id"

        private const val ENV_VAR_ENROLLMENT_COUNT = "enrollment_count"

        private const val ENV_VAR_INCIDENT_DATE = "incident_date"

        private const val ENV_VAR_TEI_COUNT = "tei_count"

        private const val ENV_VAR_EVENT_STATUS = "event_status"

        private const val ENV_VAR_OU = "org_unit"

        private const val ENV_VAR_ENROLLMENT_STATUS = "enrollment_status"

        private const val ENV_VAR_PROGRAM_STAGE_ID = "program_stage_id"

        private const val ENV_VAR_PROGRAM_STAGE_NAME = "program_stage_name"

        private const val ENV_VAR_PROGRAM_NAME = "program_name"

        private const val ENV_VAR_ENVIRONMENT = "environment"

        private const val ENV_VAR_OU_CODE = "orgunit_code"

        @JvmStatic
        fun target(ruleEnrollment: RuleEnrollment) = RuleVariableValueMapBuilder(ruleEnrollment)

        @JvmStatic
        fun target(ruleEvent: RuleEvent) = RuleVariableValueMapBuilder(ruleEvent)

        @JvmStatic
        private fun isEventInList(ruleEvents: List<RuleEvent>, ruleEvent: RuleEvent?): Boolean {
            ruleEvent?.let {
                ruleEvents.forEach { event ->
                    if (event.event == it.event)
                        return true
                }
            }
            return false
        }
    }
}
