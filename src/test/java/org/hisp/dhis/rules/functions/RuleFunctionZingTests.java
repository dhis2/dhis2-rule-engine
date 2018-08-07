package org.hisp.dhis.rules.functions;

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

import org.hisp.dhis.rules.RuleVariableValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class RuleFunctionZingTests
{
    @Test
    public void evaluateD2Zing()
    {
        RuleFunction zing = RuleFunctionZing.create();

        String zingPositive = zing.evaluate( Arrays.asList( "5" ),
                new HashMap<String, RuleVariableValue>(), null);

        assertThat( zingPositive ).isEqualTo( "5" );

        String zingNegative = zing.evaluate( Arrays.asList( "-5" ),
                new HashMap<String, RuleVariableValue>(), null);

        assertThat( zingNegative ).isEqualTo( "0" );

        String zingZero = zing.evaluate( Arrays.asList( "0" ),
                new HashMap<String, RuleVariableValue>(), null);

        assertThat( zingZero ).isEqualTo( "0" );

        String zingFloat = zing.evaluate( Arrays.asList( "5.6" ),
                new HashMap<String, RuleVariableValue>(), null);

        assertThat( zingFloat ).isEqualTo( "5.6" );
    }

    @Test
    public void evaluateMustFailOnWrongArgumentCount()
    {
        try
        {
            RuleFunctionZing.create().evaluate( Arrays.asList( "abc" ),
                    new HashMap<String, RuleVariableValue>(), null );
            fail( "Invalid number format" );
        }
        catch ( IllegalArgumentException illegalArgumentException )
        {
            // noop
        }
    }
}
