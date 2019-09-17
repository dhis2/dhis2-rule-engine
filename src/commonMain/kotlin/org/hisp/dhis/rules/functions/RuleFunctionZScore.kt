package org.hisp.dhis.rules.functions

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.rules.RuleVariableValue
import org.hisp.dhis.rules.utils.DecimalFormat


abstract class RuleFunctionZScore : RuleFunction() {

    abstract val tableForGirl: Map<ZScoreTableKey, Map<Float, Int>>

    abstract val tableForBoy: Map<ZScoreTableKey, Map<Float, Int>>

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?,
                          supplementaryData: Map<String, List<String>>?): String {

        if (arguments.size < 3) {
            throw IllegalArgumentException("At least three arguments required but found: ${arguments.size}")
        }

        // 1 = female, 0 = male
        val parameter: Float
        val weight: Float
        val gender = if (GENDER_CODES.contains(arguments[2])) 0.toByte() else 1.toByte()

        val zScore: String

        try {
            parameter = arguments[0]!!.toFloat()
            weight = arguments[1]!!.toFloat()
        } catch (ex: NumberFormatException) {
            throw IllegalArgumentException("Byte parsing failed")
        }

        zScore = getZScore(parameter, weight, gender)

        return zScore
    }

    private fun getZScore(parameter: Float, weight: Float, gender: Byte): String {
        val key = ZScoreTableKey(gender, parameter)

        val sdMap: Map<Float, Int>

        // Female
        if (gender.toInt() == 1) {
            sdMap = tableForGirl.getValue(key)
        } else {
            sdMap = tableForBoy.getValue(key)
        }

        val multiplicationFactor = getMultiplicationFactor(sdMap, weight)

        // weight exactly matches with any of the SD values
        if (sdMap.keys.contains(weight)) {
            val sd = sdMap.getValue(weight)

            return sd.times(multiplicationFactor).toString()
        }

        // weight is beyond -3SD or 3SD
        if (weight > sdMap.keys.max() ?: 0f) {
            return 3.5.toString()
        } else if (weight < sdMap.keys.min() ?: 0f) {
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
            gap = weight - lowerLimitX
            decimalAddition = gap / distance
            result = sdMap.getValue(lowerLimitX) + decimalAddition
        } else {
            gap = higherLimitY - weight
            decimalAddition = gap / distance
            result = sdMap.getValue(higherLimitY) + decimalAddition
        }

        result *= multiplicationFactor

        return format.format(result.toDouble()).toString()
    }

    private fun getMultiplicationFactor(sdMap: Map<Float, Int>, weight: Float): Int {
        val median = findMedian(sdMap)

        return weight.compareTo(median)
    }

    private fun findMedian(sdMap: Map<Float, Int>): Float {
        val sortedList = sortKeySet(sdMap)

        return sortedList[3]
    }

    private fun sortKeySet(sdMap: Map<Float, Int>): List<Float> {
        val keySet = sdMap.keys

        val list = ArrayList(keySet)

        list.sort()

        return list
    }

    companion object {
        private val GENDER_CODES = hashSetOf("male", "MALE", "Male", "ma", "m", "M", "0", "false")
        protected val format = DecimalFormat("###.00")
    }
}
