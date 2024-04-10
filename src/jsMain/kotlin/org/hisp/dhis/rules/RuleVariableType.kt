package org.hisp.dhis.rules

@JsExport
@OptIn(ExperimentalJsExport::class)
enum class RuleVariableType {
    DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE,
    DATAELEMENT_NEWEST_EVENT_PROGRAM,
    DATAELEMENT_CURRENT_EVENT,
    DATAELEMENT_PREVIOUS_EVENT,
    CALCULATED_VALUE,
    TEI_ATTRIBUTE;
}