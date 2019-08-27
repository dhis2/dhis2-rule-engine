package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue

abstract class RuleFunction {

    abstract fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?,
                          supplementaryData: Map<String, List<String>>?): String

    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"

        @JvmStatic
        fun create(function: String): RuleFunction? {
            return when (function) {
                RuleFunctionDaysBetween.D2_DAYS_BETWEEN -> RuleFunctionDaysBetween.create()
                RuleFunctionWeeksBetween.D2_WEEKS_BETWEEN -> RuleFunctionWeeksBetween.create()
                RuleFunctionHasValue.D2_HAS_VALUE -> RuleFunctionHasValue.create()
                RuleFunctionFloor.D2_FLOOR -> RuleFunctionFloor.create()
                RuleFunctionCeil.D2_CEIL -> RuleFunctionCeil.create()
                RuleFunctionAddDays.D2_ADD_DAYS -> RuleFunctionAddDays.create()
                RuleFunctionLastEventDate.D2_LAST_EVENT_DATE -> RuleFunctionLastEventDate.create()
                RuleFunctionCountIfValue.D2_COUNT_IF_VALUE -> RuleFunctionCountIfValue.create()
                RuleFunctionRound.D2_ROUND -> RuleFunctionRound.create()
                RuleFunctionModulus.D2_MODULUS -> RuleFunctionModulus.create()
                RuleFunctionLength.D2_LENGTH -> RuleFunctionLength.create()
                RuleFunctionSplit.D2_SPLIT -> RuleFunctionSplit.create()
                RuleFunctionCount.D2_COUNT -> RuleFunctionCount.create()
                RuleFunctionSubString.D2_SUBSTRING -> RuleFunctionSubString.create()
                RuleFunctionMonthsBetween.D2_MONTHS_BETWEEN -> RuleFunctionMonthsBetween.create()
                RuleFunctionYearsBetween.D2_YEARS_BETWEEN -> RuleFunctionYearsBetween.create()
                RuleFunctionZpvc.D2_ZPVC -> RuleFunctionZpvc.create()
                RuleFunctionZScoreWFA.D2_ZSCOREWFA -> RuleFunctionZScoreWFA.create()
                RuleFunctionZScoreHFA.D2_ZSCOREHFA -> RuleFunctionZScoreHFA.create()
                RuleFunctionZScoreWFH.D2_ZSCOREWFH -> RuleFunctionZScoreWFH.create()
                RuleFunctionMaxValue.D2_MAX_VALUE -> RuleFunctionMaxValue.create()
                RuleFunctionMinValue.D2_MIN_VALUE -> RuleFunctionMinValue.create()
                RuleFunctionZing.D2_ZING -> RuleFunctionZing.create()
                RuleFunctionOizp.D2_OIZP -> RuleFunctionOizp.create()
                RuleFunctionCountIfZeroPos.D2_COUNT_IF_ZERO_POS -> RuleFunctionCountIfZeroPos.create()
                RuleFunctionLeft.D2_LEFT -> RuleFunctionLeft.create()
                RuleFunctionRight.D2_RIGHT -> RuleFunctionRight.create()
                RuleFunctionValidatePattern.D2_VALIDATE_PATTERN -> RuleFunctionValidatePattern.create()
                RuleFunctionConcatenate.D2_CONCATENATE -> RuleFunctionConcatenate.create()
                RuleFunctionInOrgUnitGroup.D2_IN_ORG_UNIT_GROUP -> RuleFunctionInOrgUnitGroup.create()
                RuleFunctionHasUserRole.D2_HAS_USER_ROLE -> RuleFunctionHasUserRole.create()
                else -> null
            }
        }
    }
}
