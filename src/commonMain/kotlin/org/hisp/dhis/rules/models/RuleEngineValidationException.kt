package org.hisp.dhis.rules.models

class RuleEngineValidationException(
    cause: IllegalArgumentException,
) : IllegalArgumentException(cause.message) 
