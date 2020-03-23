package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

abstract class RuleActionMessage
    extends RuleActionData
{

    @Nonnull
    public abstract String content();

    @Nonnull
    public abstract String field();
}
