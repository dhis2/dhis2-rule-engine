package org.dhis2.ruleengine
/*
 * Copyright (c) 2004-2020, University of Oslo
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
import kotlinx.datetime.*
import org.dhis2.ruleengine.models.Rule
import org.dhis2.ruleengine.models.RuleEnrollment
import org.dhis2.ruleengine.models.RuleEvent
import org.dhis2.ruleengine.models.RuleVariable
import kotlin.test.assertTrue

/**
 * @author Zubair Asghar
 */
object RuleEngineTestUtils {

    fun getRuleEngine(rule: Rule, ruleVariables: List<RuleVariable>): RuleEngine {
        return buildRuleEngine(rule = listOf(rule), ruleVariables = ruleVariables)
    }

    fun getRuleEngine(
        rules: List<Rule>,
        ruleVariables: List<RuleVariable> = emptyList(),
        ruleEnrollment: RuleEnrollment? = null,
        ruleEvents: List<RuleEvent> = emptyList(),
        supplementaryData: Map<String,List<String>> = emptyMap(),
        constantsValue:Map<String,String> = emptyMap(),
        dataItemStore:Map<String, DataItem> = emptyMap()
    ): RuleEngine {
        return buildRuleEngine(
            rule = rules,
            ruleVariables = ruleVariables,
            ruleEnrollment = ruleEnrollment,
            ruleEvents = ruleEvents,
            supplementaryData = supplementaryData,
            constantsValue = constantsValue,
            dataItemStore = dataItemStore
        )
    }

    private fun buildRuleEngine(
        rule: List<Rule> = emptyList(),
        ruleVariables: List<RuleVariable> = listOf(),
        ruleEnrollment: RuleEnrollment? = null,
        ruleEvents: List<RuleEvent> = emptyList(),
        supplementaryData: Map<String,List<String>> = emptyMap(),
        constantsValue:Map<String,String> = emptyMap(),
        dataItemStore:Map<String, DataItem> = emptyMap()
    ): RuleEngine {
        return RuleEngineContext(
            rules = rule,
            ruleVariables = ruleVariables,
            supplementaryData = supplementaryData,
            constantsValues = constantsValue,
            dataItemStore = dataItemStore
        )
            .toRuleEngine(ruleEvents, ruleEnrollment)
    }

    fun currentDate() = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    fun LocalDate.add(days:Int): LocalDate {

        return if(days>0) {
            this.plus(days, DateTimeUnit.DAY)
        }else{
            this.minus(days, DateTimeUnit.DAY)
        }
    }

    fun assertThrowsIllegalArgumentException(method: () -> Unit) {
        try {
            method.invoke()
        } catch (e: Exception) {
            assertTrue { e is IllegalArgumentException }
        }
    }
}
