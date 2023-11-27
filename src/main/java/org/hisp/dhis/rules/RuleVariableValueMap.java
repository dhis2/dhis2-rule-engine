package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;

import javax.annotation.Nonnull;
import java.util.Map;

public record RuleVariableValueMap(
        @Nonnull Map<RuleEnrollment, Map<String, RuleVariableValue>> enrollmentMap,
        @Nonnull Map<RuleEvent, Map<String, RuleVariableValue>> eventMap
) {}