package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public enum RuleValueType
{
    TEXT( "" ), NUMERIC( 0d ), BOOLEAN( false ), DATE( "2020-01-01" );

    @Nonnull
    private final Object defaultValue;

    RuleValueType( @Nonnull Object defaultValue )
    {
        this.defaultValue = defaultValue;
    }

    @Nonnull
    public Object defaultValue()
    {
        return defaultValue;
    }
}
