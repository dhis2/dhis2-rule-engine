package org.hisp.dhis.rules;

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

import org.apache.commons.lang3.StringUtils;
import org.hisp.dhis.rules.models.AttributeType;
import org.hisp.dhis.rules.models.Rule;
import org.hisp.dhis.rules.models.RuleAction;
import org.hisp.dhis.rules.models.RuleActionAttribute;
import org.hisp.dhis.rules.models.RuleEnrollment;
import org.hisp.dhis.rules.models.RuleEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Enrico Colasante
 */
class RuleEngineFilter
{
    static List<Rule> filterRules( List<Rule> rules, RuleEnrollment ruleEnrollment )
    {
        List<Rule> filteredRules = new ArrayList<>();

        for ( Rule rule : rules )
        {
            if ( StringUtils.isEmpty( rule.programStage() ) )
            {
                List<RuleAction> ruleActions = filterActionRules( rule.actions(),
                    AttributeType.TRACKED_ENTITY_ATTRIBUTE );
                filteredRules.add( Rule.copy( rule, ruleActions ) );
            }
        }

        return filteredRules;
    }

    static List<Rule> filterRules( List<Rule> rules, RuleEvent ruleEvent )
    {
        List<Rule> filteredRules = new ArrayList<>();

        for ( Rule rule : rules )
        {
            if ( StringUtils.isEmpty( rule.programStage() ) ||
                Objects.equals( rule.programStage(), ruleEvent.programStage() ) )
            {
                List<RuleAction> ruleActions = filterActionRules( rule.actions(),
                    AttributeType.DATA_ELEMENT );
                filteredRules.add( Rule.copy( rule, ruleActions ) );
            }
        }

        return filteredRules;
    }

    private static List<RuleAction> filterActionRules( List<RuleAction> ruleActions, AttributeType attributeType )
    {
        List<RuleAction> filteredRuleActions = new ArrayList<>();

        for ( RuleAction ruleAction : ruleActions )
        {
            if ( !(ruleAction instanceof RuleActionAttribute) ||
                (((RuleActionAttribute) ruleAction).attributeType() == attributeType ||
                    ((RuleActionAttribute) ruleAction).attributeType() == AttributeType.UNKNOWN) )
            {
                filteredRuleActions.add( ruleAction );
            }
        }

        return filteredRuleActions;
    }
}
