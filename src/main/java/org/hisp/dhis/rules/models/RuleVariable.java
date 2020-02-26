package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.RuleVariableValueMapBuilder;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * ToDo - add support for next properties:
 *   1) Boolean useCode()
 *   2) List<Option> options()
 */
public abstract class RuleVariable
{
        /**
         * @return Name of the variable. Something what users refer to
         * when building program rules.
         */
        @Nonnull
        public abstract String name();

        public abstract Map<String, RuleVariableValue> createValues( RuleVariableValueMapBuilder builder,
            Map<String, List<RuleDataValue>> allEventValues,
            Map<String, RuleAttributeValue> currentEnrollmentValues,
            Map<String, RuleDataValue> currentEventValues );
}
