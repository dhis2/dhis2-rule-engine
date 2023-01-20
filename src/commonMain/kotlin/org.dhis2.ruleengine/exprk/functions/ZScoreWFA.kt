package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.zscore.ZScoreTable
import org.dhis2.ruleengine.zscore.ZScoreTableKey
import kotlin.math.floor

const val ZSCORE_WFA = "d2:zScoreWFA"
class ZScoreWFA : ZScore() {
    override fun getTableForGirl(): Map<ZScoreTableKey, Map<Float, Int>> = ZScoreTable.zscoreWFATableGirl

    override fun getTableForBoy(): Map<ZScoreTableKey, Map<Float, Int>> = ZScoreTable.zscoreWFATableBoy

    override fun validateParameter(parameter: Float) {
        if (parameter < 0 || parameter > 60) {
            throw IllegalArgumentException("age parameter should be between 0 and 60")
        }
    }

    override fun parameterCorrection(parameter: Float): Float {
        return floor(parameter)
    }
}