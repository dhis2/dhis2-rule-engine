package org.hisp.dhis.rules.models;

import org.hisp.dhis.rules.Option;

import javax.annotation.Nonnull;
import java.util.List;

abstract class RuleVariableDataElement
    extends RuleVariable
{

    @Nonnull
    public abstract String dataElement();

    @Nonnull
    public abstract RuleValueType dataElementType();

    public String getOptionName(List<Option> options, RuleDataValue value )
    {
        // if no option found then existing value in the context will be used
        String optionName = value.value();

        for ( Option op : options() )
        {
            if (op.getCode().equals( value.value() ) )
            {
                optionName = op.getName();
            }
        }

        return optionName;
    }
}
