package org.hisp.dhis.rules.functions

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import org.assertj.core.api.Java6Assertions.assertThat

@RunWith(JUnit4::class)
class RuleFunctionTests {

    @Test
    fun createMustReturnCorrectFunction() {
        assertThat(RuleFunction.create("d2:daysBetween"))
                .isInstanceOf(RuleFunctionDaysBetween::class.java)
        assertThat(RuleFunction.create("d2:fake")).isNull()
    }
}
