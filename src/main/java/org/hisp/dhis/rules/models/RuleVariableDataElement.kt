package org.hisp.dhis.rules.models

import javax.annotation.Nonnull

internal interface RuleVariableDataElement : RuleVariable {
    @Nonnull
    fun dataElement(): String

    @Nonnull
    fun dataElementType(): RuleValueType
}
