package org.hisp.dhis.rules.api

import org.hisp.dhis.rules.utils.RuleEngineUtils

object EnvironmentVariables {
    val ENV_VARIABLES = mapOf(
        Pair(RuleEngineUtils.ENV_VAR_COMPLETED_DATE, ItemValueType.DATE),
        Pair(RuleEngineUtils.ENV_VAR_CURRENT_DATE, ItemValueType.DATE),
        Pair(RuleEngineUtils.ENV_VAR_EVENT_DATE, ItemValueType.DATE),
        Pair(RuleEngineUtils.ENV_VAR_INCIDENT_DATE, ItemValueType.DATE),
        Pair(RuleEngineUtils.ENV_VAR_ENROLLMENT_DATE, ItemValueType.DATE),
        Pair(RuleEngineUtils.ENV_VAR_DUE_DATE, ItemValueType.DATE),
        Pair(RuleEngineUtils.ENV_VAR_EVENT_COUNT, ItemValueType.NUMBER),
        Pair(RuleEngineUtils.ENV_VAR_TEI_COUNT, ItemValueType.NUMBER),
        Pair(RuleEngineUtils.ENV_VAR_ENROLLMENT_COUNT, ItemValueType.NUMBER),
        Pair(RuleEngineUtils.ENV_VAR_EVENT_ID, ItemValueType.NUMBER),
        Pair(RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_ID, ItemValueType.NUMBER),
        Pair(RuleEngineUtils.ENV_VAR_ENROLLMENT_ID, ItemValueType.NUMBER),
        Pair(RuleEngineUtils.ENV_VAR_ENROLLMENT_STATUS, ItemValueType.TEXT),
        Pair(RuleEngineUtils.ENV_VAR_EVENT_STATUS, ItemValueType.TEXT),
        Pair(RuleEngineUtils.ENV_VAR_OU, ItemValueType.TEXT),
        Pair(RuleEngineUtils.ENV_VAR_OU_CODE, ItemValueType.TEXT),
        Pair(RuleEngineUtils.ENV_VAR_ENVIRONMENT, ItemValueType.TEXT),
        Pair(RuleEngineUtils.ENV_VAR_PROGRAM_NAME, ItemValueType.TEXT),
        Pair(RuleEngineUtils.ENV_VAR_PROGRAM_STAGE_NAME, ItemValueType.TEXT)
    )
}