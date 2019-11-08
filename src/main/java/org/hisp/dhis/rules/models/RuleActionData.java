package org.hisp.dhis.rules.models;

import javax.annotation.Nonnull;

public abstract class RuleActionData extends RuleAction
{
    @Nonnull
    public abstract String data();
}
