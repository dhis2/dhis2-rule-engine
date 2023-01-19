package org.dhis2.ruleengine.functions

import org.dhis2.ruleengine.parser.expression.CommonExpressionVisitor
import org.dhis2.ruleengine.parser.expression.function.ScalarFunctionToEvaluate
import org.hisp.dhis.antlr.AntlrParserUtils
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

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
 */
class RuleFunctionAddDays : ScalarFunctionToEvaluate() {
    /**
     * Function which will return the the date after adding/subtracting number of days.
     *
     * @param inputDate the date to add/subtract from.
     * @param days      number of days to add/subtract.
     * @return date after adding/subtracting days.
     */
    private fun addDays(inputDate: String, days: String): String {
        val localDate = LocalDate.parse(inputDate, DateTimeFormat.forPattern(RuleFunction.DATE_PATTERN))
        return localDate.plusDays(java.lang.Double.valueOf(days).toInt()).toString(RuleFunction.DATE_PATTERN)
    }

    override fun evaluate(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        return addDays(visitor.castStringVisit(ctx.expr(0)), visitor.castStringVisit(ctx.expr(1)))
    }

    override fun getDescription(ctx: ExprContext, visitor: CommonExpressionVisitor): Any {
        AntlrParserUtils.castDate(visitor.visit(ctx.expr(0)))
        AntlrParserUtils.castDouble(visitor.visit(ctx.expr(1)))
        return CommonExpressionVisitor.DEFAULT_DATE_VALUE
    }
}