package org.hisp.dhis.rules.utils


import org.hisp.dhis.rules.models.RuleDataValue
import kotlin.jvm.JvmStatic

class Utils {

    companion object {

        @JvmStatic
        fun values(ruleDataValues: List<RuleDataValue>) = ruleDataValues.map { it.value!! }
    }

}
