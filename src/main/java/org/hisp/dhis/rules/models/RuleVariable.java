package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;


public abstract class RuleVariable
{
    /**
     * @return Name of the variable. Something what users refer to
     * when building program rules.
     */
    @Nonnull
    public abstract String name();

    public abstract boolean useCodeForOptionSet();

    @Nonnull
    public abstract List<Option> options();

    public abstract Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
        Map<String, List<RuleDataValue>> allEventValues,
        Map<String, RuleAttributeValue> currentEnrollmentValues,
        Map<String, RuleDataValue> currentEventValues );
}
