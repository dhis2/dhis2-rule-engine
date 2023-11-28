package org.hisp.dhis.rules;

import org.hisp.dhis.lib.expression.spi.ValueType;
import org.hisp.dhis.lib.expression.spi.VariableValue;
import org.hisp.dhis.rules.models.RuleValueType;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public record RuleVariableValue(
        @CheckForNull
        String value,
        @Nonnull
        RuleValueType type,
        @Nonnull
        List<String> candidates,
        @CheckForNull
        String eventDate
) implements VariableValue {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Nonnull
    public static RuleVariableValue create( @Nonnull RuleValueType ruleValueType )
    {
        return new RuleVariableValue( null, ruleValueType, List.of(), getFormattedDate( new Date() ) );
    }

    @Nonnull
    public static RuleVariableValue create( @Nonnull String value,
        @Nonnull RuleValueType ruleValueType )
    {
        return new RuleVariableValue( value, ruleValueType,
            List.of(), getFormattedDate( new Date() ) );
    }

    @Nonnull
    public static RuleVariableValue create( @Nonnull String value,
        @Nonnull RuleValueType ruleValueType, @Nonnull List<String> candidates, @Nonnull String eventDate ) {
        return new RuleVariableValue(value, ruleValueType,
                List.copyOf(candidates), eventDate);
    }

    private static String getFormattedDate( Date date )
    {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern( DATE_PATTERN );

        return format.format( date );
    }

    @Override
    public final ValueType valueType() {
        return switch (type()) {
            case DATE -> ValueType.DATE;
            case NUMERIC -> ValueType.NUMBER;
            case BOOLEAN -> ValueType.BOOLEAN;
            default -> ValueType.STRING;
        };
    }

    @Override
    public final Object valueOrDefault() {
        String value = value();
        return value != null ? value : type().defaultValue();
    }
}
