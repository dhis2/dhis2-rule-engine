package org.hisp.dhis.rules;

import com.google.auto.value.AutoValue;

import org.hisp.dhis.rules.models.RuleValueType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class RuleVariableValue {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String NUMBER_PATTERN = "0.0";

    @Nullable
    public abstract String value();

    @Nonnull
    public abstract RuleValueType type();

    @Nonnull
    public abstract List<String> candidates();

    @Nullable
    public abstract String eventDate();


    @Nonnull
    static RuleVariableValue create(@Nonnull RuleValueType ruleValueType) {
        return new AutoValue_RuleVariableValue(null, ruleValueType,
                Collections.unmodifiableList(new ArrayList<String>()), getFormattedDate(new Date()));
    }

    @Nonnull
    static RuleVariableValue create(@Nonnull String value,
                                    @Nonnull RuleValueType ruleValueType) {
        if (ruleValueType == null) {
            throw new IllegalArgumentException("Invalid value type");
        }
        // clean-up the value before processing it
        String processedValue = value == null ? null : value.replace("'", "");

        // if text processedValue, wrap it
        if (RuleValueType.TEXT.equals(ruleValueType)) {
            processedValue = String.format(Locale.US, "%s", processedValue);
        }

        /*if (RuleValueType.NUMERIC.equals(ruleValueType)) {TODO: UNCOMMENT WHEN VALUE FORMAT IN CLIENT IS READY
            processedValue = getFormattedNumber(value);
        }*/

        return new AutoValue_RuleVariableValue(processedValue, ruleValueType,
                Collections.unmodifiableList(new ArrayList<String>()), getFormattedDate(new Date()));
    }

    @Nonnull
    static RuleVariableValue create(@Nonnull String value,
                                    @Nonnull RuleValueType ruleValueType, @Nonnull List<String> candidates, @Nonnull String eventDate) {
        if (candidates == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }
        // clean-up the value before processing it
        String processedValue = value.replace("'", "");

        // if text processedValue, wrap it
        if (RuleValueType.TEXT.equals(ruleValueType)) {
            processedValue = String.format(Locale.US, "%s", processedValue);
        }

       /* if (RuleValueType.NUMERIC.equals(ruleValueType)) {TODO: UNCOMMENT WHEN VALUE FORMAT IN CLIENT IS READY
            processedValue = getFormattedNumber(value);
        }*/

        return new AutoValue_RuleVariableValue(processedValue, ruleValueType,
                Collections.unmodifiableList(candidates), eventDate);
    }

    private static String getFormattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(DATE_PATTERN);

        return format.format(date);
    }

    private static String getFormattedNumber(String number) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator('.');
        return new DecimalFormat(NUMBER_PATTERN, otherSymbols).format(Float.valueOf(number));
    }
}
