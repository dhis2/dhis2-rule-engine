package org.hisp.dhis.rules.models;

import org.hisp.dhis.lib.expression.spi.ParseException;

public class RuleEngineValidationException extends ParseException {
    public RuleEngineValidationException(String s) {
        super(s);
    }
}