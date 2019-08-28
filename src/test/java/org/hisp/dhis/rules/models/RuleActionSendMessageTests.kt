package org.hisp.dhis.rules.models

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import org.assertj.core.api.Java6Assertions.assertThat

@RunWith(JUnit4::class)
class RuleActionSendMessageTests {

    @Test
    fun substitute_empty_strings_when_create_with_null_arguments() {
        val ruleActionSendMessage = RuleActionSendMessage.create("notification", "data")
        val ruleActionSendMessageNoData = RuleActionSendMessage.create("notification", null)
        val ruleActionSendMessageNoNotification = RuleActionSendMessage.create(null, "data")

        assertThat(ruleActionSendMessage.notification).isEqualTo("notification")
        assertThat(ruleActionSendMessage.data).isEqualTo("data")

        assertThat(ruleActionSendMessageNoData.notification).isEqualTo("notification")
        assertThat(ruleActionSendMessageNoData.data).isEqualTo("")

        assertThat(ruleActionSendMessageNoNotification.notification).isEqualTo("")
        assertThat(ruleActionSendMessageNoNotification.data).isEqualTo("data")
    }
}
