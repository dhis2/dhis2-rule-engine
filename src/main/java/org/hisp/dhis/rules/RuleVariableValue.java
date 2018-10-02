package org.hisp.dhis.rules;

import com.google.auto.value.AutoValue;
import org.hisp.dhis.rules.models.RuleValueType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@AutoValue
public abstract class RuleVariableValue
{

        @Nullable
        public abstract String value();

        @Nonnull
        public abstract RuleValueType type();

        @Nonnull
        public abstract List<String> candidates();

        @Nullable
        public abstract String eventDate();

        @Nonnull
        static RuleVariableValue create( @Nonnull RuleValueType ruleValueType )
        {
                return new AutoValue_RuleVariableValue( null, ruleValueType,
                    Collections.unmodifiableList( new ArrayList<String>() ), new Date().toString() );
        }

        @Nonnull
        static RuleVariableValue create( @Nonnull String value,
            @Nonnull RuleValueType ruleValueType )
        {
                if ( ruleValueType == null )
                {
                        throw new IllegalArgumentException( "Invalid value type" );
                }
                // clean-up the value before processing it
                String processedValue = value.replace( "'", "" );

                // if text processedValue, wrap it
                if ( RuleValueType.TEXT.equals( ruleValueType ) )
                {
                        processedValue = String.format( Locale.US, "'%s'", processedValue );
                }

                return new AutoValue_RuleVariableValue( processedValue, ruleValueType,
                    Collections.unmodifiableList( new ArrayList<String>() ), new Date().toString() );
        }

        @Nonnull
        static RuleVariableValue create( @Nonnull String value,
            @Nonnull RuleValueType ruleValueType, @Nonnull List<String> candidates, @Nonnull String eventDate )
        {
                if ( candidates == null )
                {
                        throw new IllegalArgumentException( "Candidate cannot be null" );
                }
                // clean-up the value before processing it
                String processedValue = value.replace( "'", "" );

                // if text processedValue, wrap it
                if ( RuleValueType.TEXT.equals( ruleValueType ) )
                {
                        processedValue = String.format( Locale.US, "'%s'", processedValue );
                }

                return new AutoValue_RuleVariableValue( processedValue, ruleValueType,
                    Collections.unmodifiableList( candidates ), eventDate );
        }
}
