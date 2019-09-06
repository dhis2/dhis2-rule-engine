package org.hisp.dhis.rules.models

import kotlin.jvm.JvmStatic


data class RuleActionShowWarning(override var content: String?,
                                 override var data: String?,
                                 override var field: String?) : RuleActionMessage() {

    companion object {

        @JvmStatic fun create(content: String?, data: String?, field: String): RuleActionShowWarning {
            return when {
                content == null && data == null -> throw IllegalArgumentException("Both content and data must not be null")
                else -> RuleActionShowWarning(content ?: "", data ?: "", field)
            }
        }

    }
}