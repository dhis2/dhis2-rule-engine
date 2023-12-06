package org.hisp.dhis.rules

import org.hisp.dhis.rules.models.*

/*
* Copyright (c) 2004-2021, University of Oslo
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
    fun filterRules(rules: List<Rule>, ruleEnrollment: RuleEnrollment?): List<Rule> {
        val filteredRules: MutableList<Rule> = mutableListOf()
        for (rule in rules) {
            val programStage: String? = rule.programStage()
            if (programStage.isNullOrEmpty()) {
                val ruleActions = filterActionRules(
                    rule.actions(),
                    AttributeType.TRACKED_ENTITY_ATTRIBUTE
                )
                filteredRules.add(rule.copy(actions = ruleActions))
            }
        }
        return filteredRules
    }

    fun filterRules(rules: List<Rule>, ruleEvent: RuleEvent): List<Rule> {
        val filteredRules: MutableList<Rule> = mutableListOf()
        for (rule in rules) {
            val programStage: String? = rule.programStage()
            if (programStage.isNullOrEmpty() || programStage == ruleEvent.programStage) {
                val ruleActions = filterActionRules(
                    rule.actions(),
                    AttributeType.DATA_ELEMENT
                )
                filteredRules.add(rule.copy(actions = ruleActions))
            }
        }
        return filteredRules
    }

    private fun filterActionRules(ruleActions: List<RuleAction>, attributeType: AttributeType): List<RuleAction> {
        val filteredRuleActions: MutableList<RuleAction> = mutableListOf()
        for (ruleAction in ruleActions) {
            if (ruleAction !is RuleActionAttribute || ruleAction.attributeType() == attributeType || ruleAction.attributeType() == AttributeType.UNKNOWN) {
                filteredRuleActions.add(ruleAction)
            }
        }
        return filteredRuleActions
    }
