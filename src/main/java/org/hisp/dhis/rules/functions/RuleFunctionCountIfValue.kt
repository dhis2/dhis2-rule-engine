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

/**
 * Counts the number of matching values that is entered for the source field in the first argument. Only occurrences that matches the second argument is counted.
 * The source field parameter is the name of one of the defined source fields in the program.
 */
class RuleFunctionCountIfValue : RuleFunction() {

    override fun evaluate(arguments: List<String?>,
                          valueMap: Map<String, RuleVariableValue>?,
                          supplementaryData: Map<String, List<String>>?): String {
        when {
            valueMap == null -> throw IllegalArgumentException("valueMap is expected")
            arguments.size != 2 -> throw IllegalArgumentException("Two arguments were expected, ${arguments.size} were supplied")
            else -> return countIfValue(arguments, valueMap)
        }
    }

    /**
     * Function which will return the count of argument[0].
     * Program rule variable at argument[0] will only be counted if it satisfy to condition which is at argument[1]
     *
     * @param arguments arguments for this function. First is the name of program rule variable and second is the condition
     * @param valueMap  key value pair containing values for each variable
     * @return count of program rule variable
     */
    private fun countIfValue(arguments: List<String?>, valueMap: Map<String, RuleVariableValue>): String {
        val variableValue = valueMap[arguments[0]]

        return when {
            variableValue != null -> variableValue.candidates().count { it == arguments[1] }.toString()
            else -> "0"
        }
    }

    companion object {
        const val D2_COUNT_IF_VALUE = "d2:countIfValue"

        @JvmStatic
        fun create() = RuleFunctionCountIfValue()
    }
}
