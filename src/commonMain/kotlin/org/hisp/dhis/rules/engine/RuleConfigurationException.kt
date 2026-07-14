package org.hisp.dhis.rules.engine

/**
 * Signals a rule-configuration problem detected while executing an action, such as a
 * malformed ASSIGN target. Reported as an error effect like an expression error, rather
 * than as an unexpected exception.
 */
internal class RuleConfigurationException(
    message: String,
) : IllegalArgumentException(message)
