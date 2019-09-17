package org.hisp.dhis.rules

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFailsWith

@RunWith(JUnit4::class)
class RuleFunctionCallTests {

    @Test
    fun fromMustReturnFunctionCallWithSingleArgument() {
        val ruleFunctionCall = RuleFunctionCall.from("d2:floor(19.4)")

        assertThat(ruleFunctionCall.functionCall).isEqualTo("d2:floor(19.4)")
        assertThat(ruleFunctionCall.functionName).isEqualTo("d2:floor")
        assertThat(ruleFunctionCall.arguments.size).isEqualTo(1)
        assertThat(ruleFunctionCall.arguments[0]).isEqualTo("19.4")
    }

    @Test
    fun fromMustReturnFunctionCallWithMultipleArguments() {
        val ruleFunctionCall = RuleFunctionCall.from("d2:some('one', 'two', 'three')")

        assertThat(ruleFunctionCall.functionCall).isEqualTo("d2:some('one', 'two', 'three')")
        assertThat(ruleFunctionCall.functionName).isEqualTo("d2:some")
        assertThat(ruleFunctionCall.arguments.size).isEqualTo(3)
        assertThat(ruleFunctionCall.arguments[0]).isEqualTo("'one'")
        assertThat(ruleFunctionCall.arguments[1]).isEqualTo("'two'")
        assertThat(ruleFunctionCall.arguments[2]).isEqualTo("'three'")
    }

    @Test
    fun fromMustReturnFunctionCallWithNoArguments() {
        val ruleFunctionCall = RuleFunctionCall.from("d2:some()")

        assertThat(ruleFunctionCall.functionCall).isEqualTo("d2:some()")
        assertThat(ruleFunctionCall.functionName).isEqualTo("d2:some")
        assertThat(ruleFunctionCall.arguments.size).isEqualTo(0)
    }

    @Test
    fun fromMustReturnFunctionCallWithBlankSpaceBetweenArguments() {
        val ruleFunctionCall = RuleFunctionCall.from("d2:some('one' , 'two')")

        assertThat(ruleFunctionCall.functionCall).isEqualTo("d2:some('one' , 'two')")
        assertThat(ruleFunctionCall.functionName).isEqualTo("d2:some")
        assertThat(ruleFunctionCall.arguments.size).isEqualTo(2)
        assertThat(ruleFunctionCall.arguments[0]).isEqualTo("'one'")
        assertThat(ruleFunctionCall.arguments[1]).isEqualTo("'two'")
    }

    @Test
    fun fromMustThrowOnNullArgument() {
        assertFailsWith<NullPointerException> {
            RuleFunctionCall.from(null)
        }
    }

    @Test
    fun fromMustReturnFunctionCallWithImmutableArguments() {
        val ruleFunctionCall = RuleFunctionCall.from("d2:some()")

        //ruleFunctionCall.arguments.add("another_argument")

        assertThat(ruleFunctionCall.arguments.size).isEqualTo(0)
    }
}
