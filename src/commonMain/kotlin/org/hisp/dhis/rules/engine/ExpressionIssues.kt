package org.hisp.dhis.rules.engine

import org.hisp.dhis.lib.expression.spi.IllegalExpressionException
import org.hisp.dhis.lib.expression.spi.Issue
import org.hisp.dhis.rules.models.RuleValidationIssue

internal fun IllegalExpressionException.validationErrors(): List<RuleValidationIssue> = getErrors().map(::toValidationIssue)

internal fun IllegalExpressionException.validationWarnings(): List<RuleValidationIssue> = getWarnings().map(::toValidationIssue)

/**
 * Lists every error and warning of the exception in a human-readable form, instead of the
 * exception message which only contains the number of errors and warnings.
 */
internal fun IllegalExpressionException.describeIssues(): String = describeIssues(validationErrors(), validationWarnings())

internal fun describeIssues(
    errors: List<RuleValidationIssue>,
    warnings: List<RuleValidationIssue>,
): String =
    (errors.map(::describeIssue) + warnings.map { "Warning: " + describeIssue(it) })
        .joinToString("; ")

private fun describeIssue(issue: RuleValidationIssue): String =
    if (issue.position.isBlank()) issue.message else "${issue.message} (in ${issue.position})"

private fun toValidationIssue(issue: Issue): RuleValidationIssue = RuleValidationIssue(issue.message, issue.position())
