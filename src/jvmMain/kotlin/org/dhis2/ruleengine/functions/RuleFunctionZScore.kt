package org.dhis2.ruleengine.functions

import com.google.common.collect.Sets
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

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
 */ /**
 * @author Zubair Asghar.
 */
abstract class RuleFunctionZScore : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {

        // 1 = female, 0 = male
        val parameter: Float
        val weight: Float
        val genderParameter = visitor.castStringVisit(ctx.expr(2))
            ?: throw IllegalArgumentException("Gender cannot be null")
        val gender = if (GENDER_CODES.contains(genderParameter)) 0.toByte() else 1.toByte()
        try {
            parameter = parameterCorrection(visitor.castStringVisit(ctx.expr(0)).toFloat())
            weight = visitor.castStringVisit(ctx.expr(1)).toFloat()
        } catch (ex: NumberFormatException) {
            throw IllegalArgumentException("Byte parsing failed")
        }
        validateParameter(parameter)
        return getZScore(parameter, weight, gender)
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        visitor.castDoubleVisit(ctx.expr(0))
        visitor.castDoubleVisit(ctx.expr(1))
        visitor.castStringVisit(ctx.expr(2))
        return CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE
    }

    private fun getTableForGender(gender: Int): Map<ZScoreTableKey, Map<Float, Int>> {
        return if (gender == 1) {
            tableForGirl
        } else {
            tableForBoy
        }
    }

    abstract val tableForGirl: Map<ZScoreTableKey, Map<Float, Int>>
    abstract val tableForBoy: Map<ZScoreTableKey, Map<Float, Int>>
    abstract fun validateParameter(parameter: Float)
    abstract fun parameterCorrection(parameter: Float): Float
    private fun getZScore(parameter: Float, weight: Float, gender: Byte): String {
        val key = ZScoreTableKey(gender, parameter)
        val sdMap: Map<Float, Int> = getMap(gender, key)
        require(!sdMap.isEmpty()) { "No key exist for provided parameters" }
        val multiplicationFactor = getMultiplicationFactor(sdMap, weight)

        // weight exactly matches with any of the SD values
        if (sdMap.containsKey(weight)) {
            val sd = sdMap[weight]!!
            return (sd * multiplicationFactor).toString()
        }

        // weight is beyond -3SD or 3SD
        if (weight > Collections.max(sdMap.keys)) {
            return 3.5.toString()
        } else if (weight < Collections.min(sdMap.keys)) {
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
            result = sdMap[lowerLimitX]!! + decimalAddition
        } else {
            gap = higherLimitY - weight
            decimalAddition = gap / distance
            result = sdMap[higherLimitY]!! + decimalAddition
        }
        result = result * multiplicationFactor
        return decimalFormat.format(result.toDouble()).toString()
    }

    private fun getMap(gender: Byte, key: ZScoreTableKey): HashMap<Float, Int> {
        val sdMap: HashMap<Float, Int>
        val table = getTableForGender(gender.toInt())
        sdMap = if (table.containsKey(key)) {
            table[key] as HashMap<Float, Int>
        } else {
            HashMap()
        }
        return sdMap
    }

    private fun getMultiplicationFactor(sdMap: Map<Float, Int>, weight: Float): Int {
        val median = findMedian(sdMap)
        return java.lang.Float.compare(weight, median)
    }

    private fun findMedian(sdMap: Map<Float, Int>): Float {
        val sortedList = sortKeySet(sdMap)
        return sortedList[3]
    }

    private fun sortKeySet(sdMap: Map<Float, Int>): List<Float> {
        val keySet = sdMap.keys
        val list: List<Float> = ArrayList(keySet)
        Collections.sort(list)
        return list
    }

    companion object {
        private val GENDER_CODES: Set<String> = Sets.newHashSet(
            "male", "MALE", "Male", "ma", "m", "M", "0",
            "false"
        )
        protected val decimalFormat: DecimalFormat
            protected get() {
                val decimalFormatSymbols = DecimalFormatSymbols.getInstance()
                decimalFormatSymbols.decimalSeparator = '.'
                return DecimalFormat("##0.00", decimalFormatSymbols)
            }
    }
}