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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

/**
 * @Author Zubair Asghar.
 */

@RunWith( JUnit4.class )
public class RuleFunctionHasUserRoleTests
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Map<String, List<String>> supplementaryData = new HashMap<>();

    private Map<String, RuleVariableValue> variableValues = new HashMap<>();

    private List<String> arguments = new ArrayList<>();

    @Test
    public void throwExceptionWhenSupplementaryDataIsNull()
    {
        thrown.expect( IllegalArgumentException.class );

        RuleFunctionHasUserRole hasUserRole = RuleFunctionHasUserRole.create();

        hasUserRole.evaluate( arguments, variableValues, supplementaryData );
    }

    @Test
    public void throwExceptionWhenArgumentListIsLessThanOne()
    {
        thrown.expect( IllegalArgumentException.class );

        RuleFunctionHasUserRole hasUserRole = RuleFunctionHasUserRole.create();

        supplementaryData.put( "USER", Arrays.asList() );

        hasUserRole.evaluate( arguments, variableValues, supplementaryData );
    }

    @Test
    public void returnTrueIfUserHasRole()
    {
        RuleFunctionHasUserRole hasUserRole = RuleFunctionHasUserRole.create();

        supplementaryData.put( "USER", Arrays.asList( "uid1" ) );
        arguments.add( "uid1" );

        assertThat( hasUserRole.evaluate( arguments, variableValues, supplementaryData ), is( "true" ) );
    }

    @Test
    public void returnFalseIfUserHasNoRole()
    {
        RuleFunctionHasUserRole hasUserRole = RuleFunctionHasUserRole.create();

        supplementaryData.put( "USER", Arrays.asList( "uid1" ) );
        arguments.add( "uid2" );

        assertThat( hasUserRole.evaluate( arguments, variableValues, supplementaryData ), is( "false" ) );
    }

    @Test
    public void returnFalseIfRoleListIsNull()
    {
        RuleFunctionHasUserRole hasUserRole = RuleFunctionHasUserRole.create();

        supplementaryData.put( "USER", null );
        arguments.add( "uid2" );

        assertThat( hasUserRole.evaluate( arguments, variableValues, supplementaryData ), is( "false" ) );
    }
}
