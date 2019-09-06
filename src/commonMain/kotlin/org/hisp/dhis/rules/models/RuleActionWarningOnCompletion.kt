package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleActionWarningOnCompletion(override var content: String?,
                                         override var data: String?,
                                         override var field: String?) : RuleActionMessage() {

    companion object {

        @JvmStatic fun create(content: String?, data: String?, field: String?): RuleActionWarningOnCompletion {
            return when {
                content == null && data == null && field == null -> throw IllegalArgumentException("Content, data and field must not be null at the same time")
                else -> RuleActionWarningOnCompletion(content ?: "", data ?: "", field ?: "")
            }
        }

    }
}