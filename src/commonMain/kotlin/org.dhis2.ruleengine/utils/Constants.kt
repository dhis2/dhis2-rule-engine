package org.dhis2.ruleengine.utils

const val ENV_VAR_CURRENT_DATE = "current_date"
const val ENV_VAR_COMPLETED_DATE = "completed_date"
const val ENV_VAR_EVENT_DATE = "event_date"
const val ENV_VAR_EVENT_COUNT = "event_count"
const val ENV_VAR_DUE_DATE = "due_date"
const val ENV_VAR_EVENT_ID = "event_id"
const val ENV_VAR_ENROLLMENT_DATE = "enrollment_date"
const val ENV_VAR_ENROLLMENT_ID = "enrollment_id"
const val ENV_VAR_ENROLLMENT_COUNT = "enrollment_count"
const val ENV_VAR_INCIDENT_DATE = "incident_date"
const val ENV_VAR_TEI_COUNT = "tei_count"
const val ENV_VAR_EVENT_STATUS = "event_status"
const val ENV_VAR_OU = "org_unit"
const val ENV_VAR_ENROLLMENT_STATUS = "enrollment_status"
const val ENV_VAR_PROGRAM_STAGE_ID = "program_stage_id"
const val ENV_VAR_PROGRAM_STAGE_NAME = "program_stage_name"
const val ENV_VAR_PROGRAM_NAME = "program_name"
const val ENV_VAR_ENVIRONMENT = "environment"
const val ENV_VAR_OU_CODE = "orgunit_code"
object EnvironmentVariables {
    fun list() = listOf(
        ENV_VAR_CURRENT_DATE,
                ENV_VAR_COMPLETED_DATE,
                ENV_VAR_EVENT_DATE,
                ENV_VAR_EVENT_COUNT,
                ENV_VAR_DUE_DATE,
                ENV_VAR_EVENT_ID,
                ENV_VAR_ENROLLMENT_DATE,
                ENV_VAR_ENROLLMENT_ID,
                ENV_VAR_ENROLLMENT_COUNT,
                ENV_VAR_INCIDENT_DATE,
                ENV_VAR_TEI_COUNT,
                ENV_VAR_EVENT_STATUS,
                ENV_VAR_OU,
                ENV_VAR_ENROLLMENT_STATUS,
                ENV_VAR_PROGRAM_STAGE_ID,
                ENV_VAR_PROGRAM_STAGE_NAME,
                ENV_VAR_PROGRAM_NAME,
                ENV_VAR_ENVIRONMENT,
                ENV_VAR_OU_CODE
    )
}