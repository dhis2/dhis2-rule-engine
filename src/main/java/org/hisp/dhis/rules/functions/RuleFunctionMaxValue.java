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

import com.google.common.collect.Lists;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.rules.parser.expression.function.ScalarFunctionToEvaluate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

/**
 * @Author Zubair Asghar.
 * <p>
 * return maximum value of provided data element present in entire enrollment
 */
public class RuleFunctionMaxValue
    extends ScalarFunctionToEvaluate
{
    private String getMaxValue( String dataElement, Map<String, RuleVariableValue> valueMap )
    {
        if ( valueMap.containsKey( dataElement ) )
        {
            RuleVariableValue ruleVariableValue = valueMap.get( dataElement );

            List<String> values = ruleVariableValue.candidates();

            List<Double> doubles = Lists.newArrayList();

            for ( String value : values )
            {
                doubles.add( Double.parseDouble( value ) );
            }

            return Collections.max( doubles ).toString();
        }

        return "";
    }

    @Override
    public Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return getMaxValue( ctx.variableName().getText(), visitor.getValueMap() );
    }
}
