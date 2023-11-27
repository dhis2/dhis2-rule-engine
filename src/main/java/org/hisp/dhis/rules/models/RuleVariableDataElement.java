package org.hisp.dhis.rules.models;


import javax.annotation.Nonnull;

interface RuleVariableDataElement extends RuleVariable {

    @Nonnull
    String dataElement();

    @Nonnull
    RuleValueType dataElementType();
}
