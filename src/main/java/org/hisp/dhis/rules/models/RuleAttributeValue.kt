package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue

data class RuleAttributeValue(val trackedEntityAttribute: String?, val value: String?) {


    companion object {

        fun create(attribute: String, value: String): RuleAttributeValue {
            return RuleAttributeValue(attribute, value)
        }
    }
}
