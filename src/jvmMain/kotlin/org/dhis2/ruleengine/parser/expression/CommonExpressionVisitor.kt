package org.dhis2.ruleengine.parser.expression
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
import org.apache.commons.lang3.Validate
import org.dhis2.ruleengine.DataItem
import org.dhis2.ruleengine.RuleVariableValue
import org.hisp.dhis.antlr.AntlrExprItem
import org.hisp.dhis.antlr.AntlrExpressionVisitor
import org.hisp.dhis.antlr.ParserExceptionWithoutContext
import org.hisp.dhis.parser.expression.antlr.ExpressionParser

/**
 * Common traversal of the ANTLR4 org.hisp.dhis.rules.parser.expression parse tree using the
 * visitor pattern.
 *
 * @author Jim Grace
 */
class CommonExpressionVisitor : AntlrExpressionVisitor() {
    /**
     * Map of ExprFunction instances to call for each org.hisp.dhis.rules.parser.expression function
     */
    private var itemMap: Map<Int, AntlrExprItem>? = null

    /**
     * Method to call within the ExprFunction instance
     */
    private var functionMethod: ExprFunctionMethod? = null

    /**
     * By default, replace nulls with 0 or ''.
     */
    private val replaceNulls = true

    /**
     * Values to use for variables in evaluating an org.hisp.dhis.rules.parser.expression.
     */
    private var valueMap: Map<String, RuleVariableValue> = HashMap<String, RuleVariableValue>()

    /**
     * Supplementary data for users and org units
     */
    var supplementaryData: Map<String, List<String>> = HashMap()
        private set

    /**
     * Used to collect the string replacements to build a description.
     */
    var itemDescriptions: MutableMap<String, String> = HashMap()
        private set

    /**
     * Used to collect program rule variables, constents and program variables.
     */
    private var itemStore: Map<String, DataItem> = HashMap<String, DataItem>()

    // -------------------------------------------------------------------------
    // Visitor methods
    // -------------------------------------------------------------------------
    override fun visitExpr(ctx: ExpressionParser.ExprContext): Any? {
        if (ctx.it != null) {
            val item: AntlrExprItem = itemMap!![ctx.it.getType()]
                ?: throw ParserExceptionWithoutContext(
                    "DataItem " + ctx.it.getText() + " not supported for this type of expression"
                )
            return functionMethod?.apply(item, ctx, this)
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
        return if (ctx.expr().size > 0) {
            visit(ctx.expr(0))
        } else visit(ctx.getChild(0))
        // All others: visit first child.
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------
    fun getValueMap(): Map<String, RuleVariableValue> {
        return valueMap
    }

    fun setValueMap(valueMap: Map<String, RuleVariableValue>) {
        this.valueMap = valueMap
    }

    fun getItemStore(): Map<String, DataItem> {
        return itemStore
    }
    // -------------------------------------------------------------------------
    // Builder
    // -------------------------------------------------------------------------
    /**
     * Builder for [CommonExpressionVisitor] instances.
     */
    class Builder {
        private val visitor: CommonExpressionVisitor

        init {
            visitor = CommonExpressionVisitor()
        }

        fun withFunctionMap(functionMap: Map<Int, AntlrExprItem>?): Builder {
            visitor.itemMap = functionMap
            return this
        }

        fun withFunctionMethod(functionMethod: ExprFunctionMethod?): Builder {
            visitor.functionMethod = functionMethod
            return this
        }

        fun withVariablesMap(valueMap: Map<String, RuleVariableValue>): Builder {
            visitor.valueMap = valueMap
            return this
        }

        fun withSupplementaryData(supplementaryData: Map<String, List<String>>): Builder {
            visitor.supplementaryData = supplementaryData
            return this
        }

        fun withItemDescriptions(itemDescriptions: MutableMap<String, String>): Builder {
            visitor.itemDescriptions = itemDescriptions
            return this
        }

        fun withIteamStore(itemStore: Map<String, DataItem>): Builder {
            visitor.itemStore = itemStore
            return this
        }

        fun validateCommonProperties(): CommonExpressionVisitor {
            Validate.notNull<Map<Int, AntlrExprItem>?>(visitor.itemMap, "Missing required property 'functionMap'")
            Validate.notNull<Any?>(visitor.functionMethod, "Missing required property 'functionMethod'")
            return visitor
        }

        fun validateAndBuildForDescription(): CommonExpressionVisitor {
            Validate.notNull<Map<String, DataItem>>(visitor.itemStore, "Missing required property 'itemStore'")
            Validate.notNull<Any?>(visitor.functionMethod, "Missing required property 'functionMethod'")
            return visitor
        }
    }

    companion object {
        /**
         * Default value for data type double.
         */
        const val DEFAULT_DOUBLE_VALUE = 1.0

        /**
         * Default value for data type date.
         */
        const val DEFAULT_DATE_VALUE = "2017-07-08"

        /**
         * Default value for data type boolean.
         */
        const val DEFAULT_BOOLEAN_VALUE = false

        /**
         * Creates a new Builder for CommonExpressionVisitor.
         *
         * @return a Builder for CommonExpressionVisitor.
         */
        fun newBuilder(): Builder {
            return Builder()
        }
    }
}