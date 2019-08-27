package org.hisp.dhis.rules.models


data class RuleAttributeValue(val trackedEntityAttribute: String?, val value: String?) {

    companion object {

        @JvmStatic
        fun create(attribute: String, value: String) = RuleAttributeValue(attribute, value)

    }
}