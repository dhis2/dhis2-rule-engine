package org.dhis2.ruleengine.exprk.functions

import org.dhis2.ruleengine.exprk.internal.Function
import org.dhis2.ruleengine.utils.decimalFormatter
import org.dhis2.ruleengine.zscore.ZScoreTableKey
import kotlin.math.roundToInt


abstract class ZScore : Function() {
    private val genderCodes = setOf("male", "MALE", "Male", "ma", "m", "M", "0", "false")
    override fun call(arguments: List<String?>): String {
        val genderParameter = arguments[2] ?: throw IllegalArgumentException("Gender can't be null")
        val gender: Byte = if (genderCodes.contains(genderParameter)) {
            0
        } else {
            1
        }
        val parameter = arguments[0]?.toFloat()?.let { parameterCorrection(it) }
            ?: throw IllegalArgumentException("zscore first parameter required to be a float number")
        val weight = arguments[1]?.toFloat()
            ?: throw IllegalArgumentException("zscore second parameter required to be a float number")

        validateParameter(parameter)
        return getZScore(parameter, weight, gender) ?: ""
    }

    private fun getTableForGender(gender: Int): Map<ZScoreTableKey, Map<Float, Int>> {
        return when (gender) {
            1 -> getTableForGirl()
            else -> getTableForBoy()
        }
    }

    abstract fun getTableForGirl(): Map<ZScoreTableKey, Map<Float, Int>>
    abstract fun getTableForBoy(): Map<ZScoreTableKey, Map<Float, Int>>
    abstract fun validateParameter(parameter: Float)
    abstract fun parameterCorrection(parameter: Float): Float

    private fun getZScore(parameter: Float, weight: Float, gender: Byte): String? {
        val key = ZScoreTableKey(gender, parameter)
        val sdMap: Map<Float, Int> = getMap(gender, key)
        if (sdMap.isEmpty()) {
            throw IllegalArgumentException("No key exist for provided parameters")
        }
        val multiplicationFactor = getMultiplicationFactor(sdMap, weight)

        // weight exactly matches with any of the SD values
        if (sdMap.containsKey(weight)) {
            val sd = sdMap[weight]!!
            return (sd * multiplicationFactor).toString()
        }

        // weight is beyond -3SD or 3SD

        if (weight > sdMap.keys.max()) {
            return 3.5.toString()
        } else if (weight < sdMap.keys.min()) {
            return (-3.5).toString()
        }
        var lowerLimitX = 0f
        var higherLimitY = 0f

        // find the interval
        for (f in sortKeySet(sdMap)) {
            if (weight > f) {
                lowerLimitX = f
                continue
            }
            higherLimitY = f
            break
        }
        val distance = higherLimitY - lowerLimitX
        val gap: Float
        val decimalAddition: Float
        var result: Float
        if (weight > findMedian(sdMap)) {
            gap = withDecimalFormat(weight - lowerLimitX).toFloat()
            decimalAddition = gap / distance
            result = sdMap[lowerLimitX]!! + decimalAddition
        } else {
            gap = withDecimalFormat(higherLimitY - weight).toFloat()
            decimalAddition = gap / distance
            result = sdMap[higherLimitY]!! + decimalAddition
        }
        println("GAP: $gap, DECIMAL ADD: $decimalAddition RESULT: $result")
        result *= multiplicationFactor
        return withDecimalFormat(result)
    }

    private fun withDecimalFormat(value: Float): String {
        return decimalFormatter().format(value, 2)
    }

    private fun getMap(gender: Byte, key: ZScoreTableKey): MutableMap<Float, Int> {
        val table = getTableForGender(gender.toInt())
        val sdMap = if (table.containsKey(key)) {
            table[key] as MutableMap<Float, Int>
        } else {
            mutableMapOf()
        }
        return sdMap
    }

    private fun getMultiplicationFactor(sdMap: Map<Float, Int>, weight: Float): Int {
        val median = findMedian(sdMap)
        weight.compareTo(median)
        return weight.compareTo(median)
    }

    private fun findMedian(sdMap: Map<Float, Int>): Float {
        val sortedList = sortKeySet(sdMap)
        return sortedList[3]
    }

    private fun sortKeySet(sdMap: Map<Float, Int>): List<Float> {
        return sdMap.keys.toList().sorted()
    }
}