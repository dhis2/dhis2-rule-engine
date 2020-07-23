package org.hisp.dhis.rules.parser.expression.function;

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

import org.hisp.dhis.antlr.AntlrExpressionVisitor;
import org.hisp.dhis.antlr.operator.AntlrOperatorLogicalOr;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;

/**
 * Logical operator: Or
 * <pre>
 *
 * Truth table (same as for SQL):
 *
 *       A      B    A or B
 *     -----  -----  ------
 *     null   null    null
 *     null   false   null
 *     null   true    true
 *
 *     false  null    null
 *     false  false   false
 *     false  true    true
 *
 *     true   null    true
 *     true   false   true
 *     true   true    true
 * </pre>
 *
 * @author Jim Grace
 */
public class OperatorLogicalOr extends ScalarFunctionToEvaluate
{
    @Override
    public Object evaluate(ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return new AntlrOperatorLogicalOr().evaluate( ctx, visitor );
    }

    @Override
    public Object getDescription( ExpressionParser.ExprContext ctx, CommonExpressionVisitor visitor )
    {
        return evaluate( ctx, (AntlrExpressionVisitor) visitor );
    }
}
