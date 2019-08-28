package org.hisp.dhis.rules.models

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RuleActionScheduleMessageTests {

    @Test
    fun substitute_empty_strings_when_create_with_null_arguments() {
        val ruleActionScheduleMessage = RuleActionScheduleMessage.create("notification", "data")
        val ruleActionScheduleMessageNoData = RuleActionScheduleMessage.create("notification", null)
        val ruleActionScheduleMessageNoNotification = RuleActionScheduleMessage.create(null, "data")

        assertThat(ruleActionScheduleMessage.notification).isEqualTo("notification")
        assertThat(ruleActionScheduleMessage.data).isEqualTo("data")

        assertThat(ruleActionScheduleMessageNoData.notification).isEqualTo("notification")
        assertThat(ruleActionScheduleMessageNoData.data).isEqualTo("")

        assertThat(ruleActionScheduleMessageNoNotification.notification).isEqualTo("")
        assertThat(ruleActionScheduleMessageNoNotification.data).isEqualTo("data")
    }
}
