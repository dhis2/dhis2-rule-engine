package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.RuleValidationResult

interface RuleEngineAPI {
    fun validate(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult

    fun validateDataFieldExpression(expression: String, dataItemStore: Map<String, DataItem>): RuleValidationResult
}