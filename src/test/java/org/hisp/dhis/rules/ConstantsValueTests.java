package org.hisp.dhis.rules;

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

import org.hisp.dhis.rules.models.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class ConstantsValueTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Map<String, String> constantsValueMap = new HashMap<>();

    @Test
    public void shouldThrowExceptionIfConstantsValueMapIsNull()
    {
       exception.expect(IllegalArgumentException.class);

        RuleEngineContext ruleEngineContext = RuleEngineContext.builder(new ExpressionEvaluator())
                .rules(Arrays.asList(mock(org.hisp.dhis.rules.models.Rule.class)))
                .ruleVariables(Arrays.asList(mock(RuleVariable.class)))
                .supplementaryData(new HashMap<>())
                .calculatedValueMap(new HashMap<>())
                .constantsValue(null)
                .build();

    }

    @Test
    public void assignConstantValueFromAssignActionInEnrollment() throws Exception
    {
        RuleAction assignAction = RuleActionAssign.Companion.create(null, "C{test_constant_value}", "test_data_element");
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule.create(null, 1, "true", Arrays.asList(assignAction), "test_program_rule1");

        constantsValueMap.put("test_constant_value", "3.14");

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList(rule));

        RuleEnrollment enrollment = RuleEnrollment.Companion.builder()
                .enrollment("test_enrollment")
                .programName("test_program")
                .incidentDate(new Date())
                .enrollmentDate(new Date())
                .status(RuleEnrollment.Status.ACTIVE)
                .organisationUnit("test_ou")
                .organisationUnitCode("test_ou_code")
                .attributeValues(Arrays.asList( RuleAttributeValue.Companion.create("test_attribute", "test_value")))
                .build();

        RuleEvent ruleEvent = RuleEvent.Companion.builder()
                .event("test_event")
                .programStage("test_program_stage")
                .programStageName("")
                .status(RuleEvent.Status.ACTIVE)
                .eventDate(new Date())
                .dueDate(new Date())
                .organisationUnit("")
                .organisationUnitCode("")
                .dataValues(Arrays.asList(RuleDataValue.Companion.create(
                        new Date(), "test_program_stage", "test_data_element", "test_value")))
                .build();

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( enrollment ).call();

        assertThat( ruleEffects.size() ).isEqualTo(1);
        assertThat( ruleEffects.get(0).data() ).isEqualTo("3.14");
        assertThat( ruleEffects.get(0).ruleAction() ).isEqualTo(assignAction);
    }

    @Test
    public void assignConstantValueFromAssignActionInEvent() throws Exception
    {
        RuleAction assignAction = RuleActionAssign.Companion.create(null, "C{test_constant_value}", "test_data_element");
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule.create(null, 1, "true", Arrays.asList(assignAction), "test_program_rule1");

        constantsValueMap.put("test_constant_value", "3.14");

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList(rule));

        RuleEvent ruleEvent = RuleEvent.Companion.builder()
                .event("test_event")
                .programStage("test_program_stage")
                .programStageName("")
                .status(RuleEvent.Status.ACTIVE)
                .eventDate(new Date())
                .dueDate(new Date())
                .organisationUnit("")
                .organisationUnitCode("")
                .dataValues(Arrays.asList(RuleDataValue.Companion.create(
                        new Date(), "test_program_stage", "test_data_element", "test_value")))
                .build();

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo(1);
        assertThat( ruleEffects.get(0).data() ).isEqualTo("3.14");
        assertThat( ruleEffects.get(0).ruleAction() ).isEqualTo(assignAction);

    }

    private RuleEngine.Builder getRuleEngine( List<org.hisp.dhis.rules.models.Rule> rules )
    {
        return RuleEngineContext
                .builder( new ExpressionEvaluator() )
                .rules( rules )
                .ruleVariables( Arrays.asList() )
                .calculatedValueMap( new HashMap<>() )
                .supplementaryData( new HashMap<>() )
                .constantsValue( constantsValueMap )
                .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }

}
