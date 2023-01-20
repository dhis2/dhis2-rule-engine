package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.zscore.ZScoreTable
import org.dhis2.ruleengine.zscore.ZScoreTableKey
import kotlin.math.floor

const val ZSCORE_HFA = "d2:zScoreHFA"
class ZScoreHFA : ZScore() {
    override fun getTableForGirl(): Map<ZScoreTableKey, Map<Float, Int>> = ZScoreTable.zscoreHFATableGirl

    override fun getTableForBoy(): Map<ZScoreTableKey, Map<Float, Int>> = ZScoreTable.zscoreHFATableBoy

    override fun validateParameter(parameter: Float) {
        if (parameter < 0 || parameter > 60) {
            throw IllegalArgumentException("age parameter should be between 0 and 60")
        }
    }

    override fun parameterCorrection(parameter: Float): Float {
        return floor(parameter)
    }
}