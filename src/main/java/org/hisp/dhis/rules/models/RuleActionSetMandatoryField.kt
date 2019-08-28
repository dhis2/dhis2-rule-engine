package org.hisp.dhis.rules.models


data class RuleActionSetMandatoryField(val field: String?) : RuleAction() {

    companion object {

        @JvmStatic
        fun create(field: String) = RuleActionSetMandatoryField(field)

    }
}