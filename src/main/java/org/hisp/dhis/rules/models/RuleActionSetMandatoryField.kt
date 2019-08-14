package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleActionSetMandatoryField(
        val field: String?
): RuleAction() {

    companion object {

        fun create(field: String?): RuleActionSetMandatoryField {
            return RuleActionSetMandatoryField(field)
        }
    }
}
