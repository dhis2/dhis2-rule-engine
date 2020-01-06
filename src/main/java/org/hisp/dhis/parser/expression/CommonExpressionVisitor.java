package org.hisp.dhis.parser.expression;

/*
 * Copyright (c) 2004-2019, University of Oslo
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

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.Validate;
import org.hisp.dhis.parser.expression.antlr.ExpressionBaseVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.parser.expression.literal.DefaultLiteral;
import org.hisp.dhis.rules.RuleVariableValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.parser.expression.ParserUtils.castBoolean;
import static org.hisp.dhis.parser.expression.ParserUtils.castDouble;
import static org.hisp.dhis.parser.expression.ParserUtils.castString;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.BooleanLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExpressionContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.NumericLiteralContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.StringLiteralContext;

/**
 * Common traversal of the ANTLR4 org.hisp.dhis.parser.expression parse tree using the
 * visitor pattern.
 *
 * @author Jim Grace
 */
public class CommonExpressionVisitor
    extends ExpressionBaseVisitor<Object>
{
    /**
     * Map of ExprFunction instances to call for each org.hisp.dhis.parser.expression function
     */
    private Map<Integer, ExprFunction> functionMap;

    /**
     * Method to call within the ExprFunction instance
     */
    private ExprFunctionMethod functionMethod;

    /**
     * Instance to call for each literal
     */
    private ExprLiteral expressionLiteral = new DefaultLiteral();

    /**
     * By default, replace nulls with 0 or ''.
     */
    private boolean replaceNulls = true;

    /**
     * Values to use for variables in evaluating an org.hisp.dhis.parser.expression.
     */
    private Map<String, RuleVariableValue> valueMap = new HashMap<>();

    /**
     * Supplementary data for users and org units
     */
    private Map<String, List<String>> supplementaryData = new HashMap<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    protected CommonExpressionVisitor()
    {
    }

    /**
     * Creates a new Builder for CommonExpressionVisitor.
     *
     * @return a Builder for CommonExpressionVisitor.
     */
    public static Builder newBuilder()
    {
        return new CommonExpressionVisitor.Builder();
    }

    // -------------------------------------------------------------------------
    // Visitor methods
    // -------------------------------------------------------------------------

    @Override
    public final Object visitExpression( ExpressionContext ctx )
    {
        return visit( ctx.expr() );
    }

    @Override
    public Object visitExpr( ExprContext ctx )
    {
        if ( ctx.fun != null )
        {
            ExprFunction function = functionMap.get( ctx.fun.getType() );

            if ( function == null )
            {
                throw new ParserExceptionWithoutContext( "Function " + ctx.fun.getText() + " not supported for this type of org.hisp.dhis.parser.expression" );
            }

            return functionMethod.apply( function, ctx, this );
        }

        if ( ctx.expr().size() > 0 ) // If there's an expr, visit the expr
        {
            return visit( ctx.expr( 0 ) );
        }

        return visit( ctx.getChild( 0 ) ); // All others: visit first child.
    }

    @Override
    public Object visitProgramRuleVariable( ExpressionParser.ProgramRuleVariableContext ctx )
    {
        RuleVariableValue variableValue = valueMap.get( ctx.uid0.getText() );
        String variable = variableValue.value() == null ?
            variableValue.type().defaultValue() : variableValue.value();

        if ( variable == null )
        {
            throw new ParserExceptionWithoutContext( "Variable " + ctx.var.getText() + " not present" );
        }

        return variable;
    }

    @Override
    public Object visitNumericLiteral( NumericLiteralContext ctx )
    {
        return expressionLiteral.getNumericLiteral( ctx );
    }

    @Override
    public Object visitStringLiteral( StringLiteralContext ctx )
    {
        return expressionLiteral.getStringLiteral( ctx );
    }

    @Override
    public Object visitBooleanLiteral( BooleanLiteralContext ctx )
    {
        return expressionLiteral.getBooleanLiteral( ctx );
    }

    @Override
    public Object visitEmpty( ExpressionParser.EmptyContext ctx )
    {
        return "";
    }

    @Override
    public Object visitTerminal( TerminalNode node )
    {
        return node.getText(); // Needed to regenerate an org.hisp.dhis.parser.expression.
    }

    // -------------------------------------------------------------------------
    // Logic for functions and items
    // -------------------------------------------------------------------------

    /**
     * Visits a context and casts the result as Double.
     *
     * @param ctx any context
     * @return the Double value
     */
    public Double castDoubleVisit( ParseTree ctx )
    {
        return castDouble( visit( ctx ) );
    }

    /**
     * Visits a context and casts the result as String.
     *
     * @param ctx any context
     * @return the Double value
     */
    public String castStringVisit( ParseTree ctx )
    {
        return castString( visit( ctx ) );
    }

    /**
     * Visits a context and casts the result as Boolean.
     *
     * @param ctx any context
     * @return the Boolean value
     */
    public Boolean castBooleanVisit( ParseTree ctx )
    {
        return castBoolean( visit( ctx ) );
    }


    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public void setValueMap( Map<String, RuleVariableValue> valueMap )
    {

        this.valueMap = valueMap;
    }

    public Map<String, RuleVariableValue> getValueMap()
    {
        return valueMap;
    }

    public Map<String, List<String>> getSupplementaryData()
    {
        return supplementaryData;
    }

    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for {@link CommonExpressionVisitor} instances.
     */
    public static class Builder
    {
        private CommonExpressionVisitor visitor;

        protected Builder()
        {
            this.visitor = new CommonExpressionVisitor();
        }

        public Builder withFunctionMap( Map<Integer, ExprFunction> functionMap )
        {
            this.visitor.functionMap = functionMap;
            return this;
        }

        public Builder withFunctionMethod( ExprFunctionMethod functionMethod )
        {
            this.visitor.functionMethod = functionMethod;
            return this;
        }

        public Builder withVariablesMap( Map<String, RuleVariableValue> valueMap )
        {
            this.visitor.valueMap = valueMap;
            return this;
        }

        public Builder withSupplementaryData( Map<String, List<String>> supplementaryData )
        {
            this.visitor.supplementaryData = supplementaryData;
            return this;
        }

        public CommonExpressionVisitor validateCommonProperties()
        {
            Validate.notNull( this.visitor.functionMap, "Missing required property 'functionMap'" );
            Validate.notNull( this.visitor.functionMethod, "Missing required property 'functionMethod'" );

            return visitor;
        }
    }
}
