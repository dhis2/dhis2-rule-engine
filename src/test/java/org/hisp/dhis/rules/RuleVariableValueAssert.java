package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleValueType;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

final class RuleVariableValueAssert
{
    private final RuleVariableValue variableValue;

    private RuleVariableValueAssert( RuleVariableValue variableValue )
    {
        this.variableValue = variableValue;
    }

    @Nonnull
    static RuleVariableValueAssert assertThatVariable( @Nonnull RuleVariableValue variableValue )
    {
        return new RuleVariableValueAssert( variableValue );
    }

    @Nonnull
    RuleVariableValueAssert hasValue( @CheckForNull String value )
    {
        assertEquals( value , variableValue.value() );
        return this;
    }

    @Nonnull
    RuleVariableValueAssert hasCandidates( @Nonnull String... candidates )
    {
        assertEquals( candidates.length , variableValue.candidates().size() );
        for ( int index = 0; index < candidates.length; index++ )
        {
            assertEquals( candidates[index] , variableValue.candidates().get( index ) );
        }
        return this;
    }

    @Nonnull
    RuleVariableValueAssert isTypeOf( @Nonnull RuleValueType valueType )
    {
        assertEquals( valueType , variableValue.type() );
        return this;
    }
}
