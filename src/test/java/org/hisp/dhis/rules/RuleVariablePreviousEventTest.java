package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.RuleDataValue;
import org.hisp.dhis.rules.models.RuleDataValueHistory;
import org.hisp.dhis.rules.models.RuleEvent;
import org.hisp.dhis.rules.models.RuleValueType;
import org.hisp.dhis.rules.models.RuleVariablePreviousEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.hisp.dhis.rules.models.RuleEvent.Status.ACTIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
@RunWith( JUnit4.class )
public class RuleVariablePreviousEventTest {
    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = today.minusDays(1);
    private final LocalDate dayBeforeYesterday = yesterday.minusDays(1);
    private final Date todayInstant = Date.from(today.atStartOfDay().toInstant(ZoneOffset.UTC));
    private final Date yesterdayInstant = Date.from(yesterday.atStartOfDay().toInstant(ZoneOffset.UTC));
    private final Date dayBeforeYesterdayInstant = Date.from(dayBeforeYesterday.atStartOfDay().toInstant(ZoneOffset.UTC));
    private final Date tomorrow = Date.from(today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
    private final Date todayMorning = Date.from(today.atTime(10, 0).toInstant(ZoneOffset.UTC));
    private final Date todayAfternoon = Date.from(today.atTime(14, 0).toInstant(ZoneOffset.UTC));

    private final RuleVariablePreviousEvent ruleVariablePreviousEvent =
            RuleVariablePreviousEvent.create("previous value", "data_element", RuleValueType.NUMERIC, true, Collections.emptyList());

    @Test
    public void shouldCreateEmptyRuleVariableValueWhenNoDataValuesArePresent() {
        RuleVariableValue ruleVariableValue = ruleVariablePreviousEvent.createValues(RuleVariableValueMapBuilder.target(event(tomorrow, tomorrow)), Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap()).get("previous value");

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type());
        assertNull(ruleVariableValue.value());
        assertNull(ruleVariableValue.eventDate());
        assertEquals(Collections.emptyList(), ruleVariableValue.candidates());
    }

    @Test
    public void shouldCreateTodayRuleVariableValueWhenEventDateIsTomorrow() {
        RuleVariableValue ruleVariableValue = ruleVariablePreviousEvent.createValues(RuleVariableValueMapBuilder.target(event(tomorrow, tomorrow)), allEventsDataValues(), Collections.emptyMap(), Collections.emptyMap()).get("previous value");

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type());
        assertEquals(todayDataValue().getValue(), ruleVariableValue.value());
        assertEquals(today.toString(), ruleVariableValue.eventDate());
        assertEquals(new HashSet<>(Arrays.asList("1", "2", "3", "4", "5")), new HashSet<>(ruleVariableValue.candidates()));
    }

    @Test
    public void shouldCreateYesterdayAfternoonRuleVariableValueWhenEventDateIsToday() {
        RuleVariableValue ruleVariableValue = ruleVariablePreviousEvent.createValues(RuleVariableValueMapBuilder.target(event(todayInstant, todayInstant)), allEventsDataValues(), Collections.emptyMap(), Collections.emptyMap()).get("previous value");

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type());
        assertEquals(yesterdayDataValueCreatedThisAfternoon().getValue(), ruleVariableValue.value());
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate());
        assertEquals(new HashSet<>(Arrays.asList("1", "2", "3", "4", "5")), new HashSet<>(ruleVariableValue.candidates()));
    }

    @Test
    public void shouldCreateYesterdayRuleVariableValueCreateTodayMorningWhenEventDateIsYesterdayAndItWasCreatedTodayAfternoon() {
        RuleVariableValue ruleVariableValue = ruleVariablePreviousEvent.createValues(RuleVariableValueMapBuilder.target(event(yesterdayInstant, todayAfternoon)), allEventsDataValues(), Collections.emptyMap(), Collections.emptyMap()).get("previous value");

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type());
        assertEquals(yesterdayDataValueCreatedThisMorning().getValue(), ruleVariableValue.value());
        assertEquals(yesterday.toString(), ruleVariableValue.eventDate());
        assertEquals(new HashSet<>(Arrays.asList("1", "2", "3", "4", "5")), new HashSet<>(ruleVariableValue.candidates()));
    }

    @Test
    public void shouldCreateDayBeforeYesterdayRuleVariableValueWhenEventDateIsYesterdayAndItWasCreatedYesterday() {
        RuleVariableValue ruleVariableValue = ruleVariablePreviousEvent.createValues(RuleVariableValueMapBuilder.target(event(yesterdayInstant, yesterdayInstant)), allEventsDataValues(), Collections.emptyMap(), Collections.emptyMap()).get("previous value");

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type());
        assertEquals(dayBeforeYesterdayDataValue().getValue(), ruleVariableValue.value());
        assertEquals(dayBeforeYesterday.toString(), ruleVariableValue.eventDate());
        assertEquals(new HashSet<>(Arrays.asList("1", "2", "3", "4", "5")), new HashSet<>(ruleVariableValue.candidates()));
    }

    @Test
    public void shouldGetNoValueWhenThereIsNoPreviousDataValue() {
        RuleVariableValue ruleVariableValue = ruleVariablePreviousEvent.createValues(RuleVariableValueMapBuilder.target(event(dayBeforeYesterdayInstant, yesterdayInstant)), allEventsDataValues(), Collections.emptyMap(), Collections.emptyMap()).get("previous value");

        assertEquals(RuleValueType.NUMERIC, ruleVariableValue.type());
        assertNull(ruleVariableValue.value());
        assertNull(ruleVariableValue.eventDate());
        assertEquals(Collections.emptyList(), ruleVariableValue.candidates());
    }

    private RuleEvent event(Date eventDate, Date createdDate) {
        return RuleEvent.create(
                "test_event",
                "test_program_stage",
                ACTIVE,
                eventDate,
                createdDate,
                new Date(),
                "",
                "",
                Arrays.asList(
                        RuleDataValue.create(eventDate, "test_program_stage","data_element", "1"),
                        RuleDataValue.create(eventDate, "test_program_stage","data_element", "2"),
                        RuleDataValue.create(eventDate, "test_program_stage","data_element", "3"),
                        RuleDataValue.create(eventDate, "test_program_stage","data_element", "4"),
                        RuleDataValue.create(eventDate, "test_program_stage","data_element", "5")
                ),
                "test_program_stage_name",
                new Date()
        );
    }

    private Map<String, List<RuleDataValueHistory>> allEventsDataValues() {
        List<RuleDataValueHistory> ruleDataValues = new ArrayList<>(
                Arrays.asList(
                        todayDataValue(),
                        yesterdayDataValueCreatedThisMorning(),
                        yesterdayDataValueCreatedThisAfternoon(),
                        tomorrowDataValue(),
                        dayBeforeYesterdayDataValue()
                )
        );

        ruleDataValues.sort(Comparator.comparing(RuleDataValueHistory::getEventDate)
                .thenComparing(RuleDataValueHistory::getCreatedDate)
                .reversed());

        return Collections.singletonMap("data_element", ruleDataValues);
    }

    private RuleDataValueHistory todayDataValue() {
        return new RuleDataValueHistory("1", todayInstant, todayInstant, "");
    }

    private RuleDataValueHistory yesterdayDataValueCreatedThisMorning() {
        return new RuleDataValueHistory("2", yesterdayInstant, todayMorning, "");
    }

    private RuleDataValueHistory yesterdayDataValueCreatedThisAfternoon() {
        return new RuleDataValueHistory("3", yesterdayInstant, todayAfternoon, "");
    }

    private RuleDataValueHistory dayBeforeYesterdayDataValue() {
        return new RuleDataValueHistory("5", dayBeforeYesterdayInstant, todayAfternoon, "");
    }

    private RuleDataValueHistory tomorrowDataValue() {
        return new RuleDataValueHistory("4", tomorrow, tomorrow, "");
    }
}