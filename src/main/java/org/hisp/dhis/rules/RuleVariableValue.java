package org.hisp.dhis.rules;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.lib.expression.spi.ValueType;
import org.hisp.dhis.lib.expression.spi.VariableValue;
import org.hisp.dhis.rules.models.RuleValueType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@AutoValue
public abstract class RuleVariableValue implements VariableValue
{
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String NUMBER_PATTERN = "0.0";

    @Nonnull
    public static RuleVariableValue create( @Nonnull RuleValueType ruleValueType )
    {
        return new AutoValue_RuleVariableValue( null, ruleValueType, List.of(), null );
    }

    @Nonnull
    public static RuleVariableValue create( @Nonnull String value,
        @Nonnull RuleValueType ruleValueType )
    {
        return new AutoValue_RuleVariableValue( value, ruleValueType,
            List.of(), null );
    }

    @Nonnull
    public static RuleVariableValue create( @Nonnull String value,
        @Nonnull RuleValueType ruleValueType, @Nonnull List<String> candidates, @Nonnull String eventDate )
    {
        return new AutoValue_RuleVariableValue( value, ruleValueType,
            Collections.unmodifiableList( candidates ), eventDate );
    }

    private static String getFormattedDate( Date date )
    {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern( DATE_PATTERN );

        return format.format( date );
    }

    @Nullable
    public abstract String value();

    @Nonnull
    public abstract RuleValueType type();

    @Nonnull
    public abstract List<String> candidates();

    @Nullable
    public abstract String eventDate();

    @Override
    public final ValueType valueType() {
        switch (type()) {
            case DATE: return ValueType.DATE;
            case NUMERIC: return ValueType.NUMBER;
            case BOOLEAN: return ValueType.BOOLEAN;
            default: return ValueType.STRING;
        }
    }

    @Override
    public final Object valueOrDefault() {
        String value = value();
        return value != null ? value : type().defaultValue();
    }
}
