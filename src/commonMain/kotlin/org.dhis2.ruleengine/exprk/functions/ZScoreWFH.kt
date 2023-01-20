package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.zscore.ZScoreTable
import org.dhis2.ruleengine.zscore.ZScoreTableKey
import kotlin.math.floor

const val ZSCORE_WFH = "d2:zScoreWFH"
class ZScoreWFH : ZScore() {
    override fun getTableForGirl(): Map<ZScoreTableKey, Map<Float, Int>> = ZScoreTable.zscoreWFHTableGirl

    override fun getTableForBoy(): Map<ZScoreTableKey, Map<Float, Int>> = ZScoreTable.zscoreWFHTableBoy

    override fun validateParameter(parameter: Float) {
        if (parameter < 45 || parameter > 120) {
            throw IllegalArgumentException("height parameter should be between 45 and 120")
        }
    }

    override fun parameterCorrection(parameter: Float): Float {
        val floorParameter = floor(parameter)
        return if (parameter - floorParameter < 0.5f) {
            floorParameter
        } else {
            floorParameter + 0.5f
        }
    }
}