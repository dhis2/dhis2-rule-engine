package org.dhis2.ruleengine.functions

import org.apache.commons.lang3.StringUtils
import org.dhis2.ruleengine.functions.RuleFunction.Companion.wrap
import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.AntlrParserUtils
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext
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
 *
 *
 * Split the text by delimiter, and keep the nth element(0 is the first).
 */
class RuleFunctionSplit : ScalarFunctionToEvaluate() {
    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        val input = visitor.castStringVisit(ctx.expr(0))
        val delimiter = visitor.castStringVisit(ctx.expr(1))
        if (input == null || delimiter == null) {
            return ""
        }
        val index = AntlrParserUtils.castDouble(visitor.castStringVisit(ctx.expr(2))).toInt()
        val tokens = Arrays.asList(*StringUtils.split(input, delimiter))
        return if (tokens.size > index && index >= 0) {
            wrap(tokens[index])
        } else ""
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        visitor.castStringVisit(ctx.expr(0))
        visitor.castStringVisit(ctx.expr(1))
        visitor.castDoubleVisit(ctx.expr(2))
        return "sample_text"
    }
}