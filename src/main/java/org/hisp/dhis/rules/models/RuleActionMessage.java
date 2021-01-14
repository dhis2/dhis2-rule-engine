package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public abstract class RuleActionMessage
    extends RuleActionAttribute
{

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String field();
}
