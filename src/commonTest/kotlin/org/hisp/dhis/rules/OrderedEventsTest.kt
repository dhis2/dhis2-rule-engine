/*
 * Copyright (c) 2004-2024, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
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

package org.hisp.dhis.rules

import kotlinx.datetime.*
import org.hisp.dhis.rules.api.RuleEngine
import org.hisp.dhis.rules.models.RuleEvent
import org.hisp.dhis.rules.models.RuleEventStatus
import org.hisp.dhis.rules.models.RuleInstant
import org.hisp.dhis.rules.models.RuleLocalDate
import org.hisp.dhis.rules.utils.currentDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class OrderedEventsTest {
    private val today = currentDate()
    private val yesterday = today.minus(1, DateTimeUnit.DAY)
    private val tomorrow = today.plus(1, DateTimeUnit.DAY)
    private val todayMorning = today.atTime(LocalTime(10, 0, 0)).toInstant(TimeZone.currentSystemDefault())
    private val todayAfternoon = today.atTime(LocalTime(14, 0, 0)).toInstant(TimeZone.currentSystemDefault())
    private val todayInstant = currentDate().atStartOfDayIn(TimeZone.currentSystemDefault())
    private val yesterdayInstant = yesterday.atStartOfDayIn(TimeZone.currentSystemDefault())
    private val tomorrowInstant = tomorrow.atStartOfDayIn(TimeZone.currentSystemDefault())

    private val ruleEngine = RuleEngine.getInstance()

    @Test
    fun shouldOrderRuleEventsByEventDate() {
        val todayEvent = event("today", today, todayInstant, null)
        val yesterdayEvent = event("yesterday", yesterday, todayInstant, null)
        val tomorrowEvent = event("tomorrow", tomorrow, todayInstant, null)

        val ruleEvents = listOf(todayEvent, yesterdayEvent, tomorrowEvent)

        val orderedEvents = ruleEngine.order(ruleEvents)

        assertEquals(listOf(yesterdayEvent, todayEvent, tomorrowEvent), orderedEvents)
    }

    @Test
    fun shouldOrderRuleEventsByEventDateAndCreatedDate() {
        val todayMorningEvent = event("today", today, todayMorning, null)
        val todayAfternoonEvent = event("yesterday", today, todayAfternoon, null)
        val yesterdayEvent = event("yesterday", yesterday, yesterdayInstant, null)
        val tomorrowEvent = event("tomorrow", tomorrow, tomorrowInstant, null)

        val ruleEvents = listOf(todayMorningEvent, yesterdayEvent, tomorrowEvent, todayAfternoonEvent)

        val orderedEvents = ruleEngine.order(ruleEvents)

        assertEquals(listOf(yesterdayEvent, todayMorningEvent, todayAfternoonEvent, tomorrowEvent), orderedEvents)
    }

    @Test
    fun shouldOrderRuleEventsByEventDateAndCreatedAtClientDate() {
        val todayMorningEvent = event("today", today, yesterdayInstant, todayMorning)
        val todayAfternoonEvent = event("yesterday", today, yesterdayInstant, todayAfternoon)
        val yesterdayEvent = event("yesterday", yesterday, yesterdayInstant, yesterdayInstant)
        val tomorrowEvent = event("tomorrow", tomorrow, yesterdayInstant, tomorrowInstant)

        val ruleEvents = listOf(todayMorningEvent, yesterdayEvent, tomorrowEvent, todayAfternoonEvent)

        val orderedEvents = ruleEngine.order(ruleEvents)

        assertEquals(listOf(yesterdayEvent, todayMorningEvent, todayAfternoonEvent, tomorrowEvent), orderedEvents)
    }

    @Test
    fun shouldOrderRuleEventsByEventDateAndCreatedDateOrCreatedAtClientDate() {
        val todayMorningEvent = event("today", today, todayMorning, null)
        val todayAfternoonEvent = event("yesterday", today, yesterdayInstant, todayAfternoon)
        val yesterdayEvent = event("yesterday", yesterday, yesterdayInstant, null)
        val tomorrowEvent = event("tomorrow", tomorrow, yesterdayInstant, tomorrowInstant)

        val ruleEvents = listOf(todayMorningEvent, yesterdayEvent, tomorrowEvent, todayAfternoonEvent)

        val orderedEvents = ruleEngine.order(ruleEvents)

        assertEquals(listOf(yesterdayEvent, todayMorningEvent, todayAfternoonEvent, tomorrowEvent), orderedEvents)
    }

    private fun event(
        event: String,
        eventDate: LocalDate,
        createdDate: Instant,
        createdAtClientDate: Instant? = null,
    ): RuleEvent =
        RuleEvent(
            event = event,
            programStage = "test_program_stage",
            programStageName = "",
            status = RuleEventStatus.ACTIVE,
            eventDate = RuleLocalDate.fromLocalDate(eventDate),
            createdDate = RuleInstant.fromInstant(createdDate),
            createdAtClientDate = createdAtClientDate?.let { RuleInstant.fromInstant(it) },
            dueDate = RuleLocalDate.currentDate(),
            completedDate = RuleLocalDate.currentDate(),
            organisationUnit = "",
            organisationUnitCode = "",
            dataValues = listOf(),
        )
}
