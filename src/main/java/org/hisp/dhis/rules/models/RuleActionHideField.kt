package org.hisp.dhis.rules.models

import com.google.auto.value.AutoValue
import javax.annotation.concurrent.Immutable

@Immutable
data class RuleActionHideField(var content: String?, var field: String?) : RuleAction() {


    companion object {

        fun create(
                content: String?, field: String): RuleActionHideField {
            return RuleActionHideField(content ?: "", field)
        }
    }
}
