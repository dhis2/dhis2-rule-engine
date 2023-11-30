package org.hisp.dhis.rules.models;

public class RuleEngineValidationException extends IllegalArgumentException {
    public RuleEngineValidationException(IllegalArgumentException cause) {
        super(cause.getMessage());
    }
}