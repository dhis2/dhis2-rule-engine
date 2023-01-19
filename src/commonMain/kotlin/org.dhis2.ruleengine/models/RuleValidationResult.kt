package org.dhis2.ruleengine.models

sealed class RuleValidationResult {
    data class Valid(
        val description: String
    ) : RuleValidationResult()

    data class Error(
        val errorMessage: String,
        val exception: Throwable
    ) : RuleValidationResult()
}
