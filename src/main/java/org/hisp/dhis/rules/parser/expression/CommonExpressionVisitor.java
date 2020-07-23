package org.hisp.dhis.rules.parser.expression;

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

import org.apache.commons.lang3.Validate;
import org.hisp.dhis.antlr.AntlrExprItem;
import org.hisp.dhis.antlr.AntlrExpressionVisitor;
import org.hisp.dhis.antlr.ParserExceptionWithoutContext;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;
import org.hisp.dhis.rules.RuleVariableValue;
import org.hisp.dhis.rules.SampleValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common traversal of the ANTLR4 org.hisp.dhis.rules.parser.expression parse tree using the
 * visitor pattern.
 *
 * @author Jim Grace
 */
public class CommonExpressionVisitor
    extends AntlrExpressionVisitor
{
    /**
     * Map of ExprFunction instances to call for each org.hisp.dhis.rules.parser.expression function
     */
    private Map<Integer, AntlrExprItem> itemMap;

    /**
     * Method to call within the ExprFunction instance
     */
    private ExprFunctionMethod functionMethod;

    /**
     * By default, replace nulls with 0 or ''.
     */
    private boolean replaceNulls = true;

    /**
     * Values to use for variables in evaluating an org.hisp.dhis.rules.parser.expression.
     */
    private Map<String, RuleVariableValue> valueMap = new HashMap<>();

    /**
     * Supplementary data for users and org units
     */
    private Map<String, List<String>> supplementaryData = new HashMap<>();

    /**
     * Used to collect the string replacements to build a description.
     */
    private Map<String, String> itemDescriptions = new HashMap<>();

    /**
     * Used to collect program rule variables, constents and program variables.
     */
    private Map<String, SampleValue> itemStore = new HashMap<>();

    /**
     * Default value for data type double.
     */
    public static final double DEFAULT_DOUBLE_VALUE = 1d;

    /**
     * Default value for data type date.
     */
    public static final String DEFAULT_DATE_VALUE = "2017-07-08";

    /**
     * Default value for data type boolean.
     */
    public static final boolean DEFAULT_BOOLEAN_VALUE = false;

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
    public Object visitExpr( ExprContext ctx )
    {

        if ( ctx.it != null )
        {
            AntlrExprItem item = itemMap.get( ctx.it.getType() );

            if ( item == null )
            {
                throw new ParserExceptionWithoutContext(
                    "Item " + ctx.it.getText() + " not supported for this type of expression" );
            }

            return functionMethod.apply( item, ctx, this );
        }

//        if (ctx.variableName() != null) {
//            RuleVariableValue variableValue = valueMap.get( ctx.uid0.getText() );
//            String variable = variableValue.value() == null ?
//                variableValue.type().defaultValue() : variableValue.value();
//
//            if ( variable == null )
//            {
//                throw new ParserExceptionWithoutContext( "Variable " + ctx.variableName().getText() + " not present" );
//            }
//
//            return variable;
//        }

        if ( ctx.expr().size() > 0 ) // If there's an expr, visit the expr
        {
            return visit( ctx.expr( 0 ) );
        }

        return visit( ctx.getChild( 0 ) ); // All others: visit first child.
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public Map<String, RuleVariableValue> getValueMap()
    {
        return valueMap;
    }

    public void setValueMap( Map<String, RuleVariableValue> valueMap )
    {

        this.valueMap = valueMap;
    }

    public Map<String, List<String>> getSupplementaryData()
    {
        return supplementaryData;
    }

    public Map<String, String> getItemDescriptions()
    {
        return itemDescriptions;
    }

    public Map<String, SampleValue> getItemStore()
    {
        return itemStore;
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

        public Builder withFunctionMap( Map<Integer, AntlrExprItem> functionMap )
        {
            this.visitor.itemMap = functionMap;
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

        public Builder withItemDescriptions( Map<String, String> itemDescriptions )
        {
            this.visitor.itemDescriptions = itemDescriptions;
            return this;
        }

        public Builder withIteamStore( Map<String, SampleValue> itemStore )
        {
            this.visitor.itemStore = itemStore;
            return this;
        }

        public CommonExpressionVisitor validateCommonProperties()
        {
            Validate.notNull( this.visitor.itemMap, "Missing required property 'functionMap'" );
            Validate.notNull( this.visitor.functionMethod, "Missing required property 'functionMethod'" );

            return visitor;
        }

        public CommonExpressionVisitor validateAndBuildForDescription()
        {
            Validate.notNull( this.visitor.itemStore, "Missing required property 'itemStore'" );
            Validate.notNull( this.visitor.functionMethod, "Missing required property 'functionMethod'" );

            return visitor;
        }
    }
}
