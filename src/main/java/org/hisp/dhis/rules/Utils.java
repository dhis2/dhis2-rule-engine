package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleEvent;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Utils
{

        public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        private Utils()
        {
                // no instances
        }

        public static List<String> values( List<RuleDataValue> ruleDataValues )
        {
                List<String> values = new ArrayList<>( ruleDataValues.size() );
                for ( RuleDataValue ruleDataValue : ruleDataValues )
                {
                        values.add( ruleDataValue.value() );
                }
                return Collections.unmodifiableList( values );
        }

        public static String getLastUpdateDateForPrevious( List<RuleDataValue> ruleDataValues,
            RuleEvent ruleEvent ) {
                List<Date> dates = new ArrayList<>();
                for (RuleDataValue date : ruleDataValues) {
                        Date d = date.eventDate();
                        if (!d.after(new Date()) && d.before(ruleEvent.eventDate())) {
                                dates.add(d);
                        }
                }
                return dateFormat.format( Collections.max(dates));
        }

        public static String getLastUpdateDate( List<RuleDataValue> ruleDataValues) {
                List<Date> dates = new ArrayList<>();
                for (RuleDataValue date : ruleDataValues) {
                        Date d = date.eventDate();
                        if (!d.after(new Date())) {
                                dates.add(d);
                        }
                }
                return dateFormat.format( Collections.max(dates));
        }
}
