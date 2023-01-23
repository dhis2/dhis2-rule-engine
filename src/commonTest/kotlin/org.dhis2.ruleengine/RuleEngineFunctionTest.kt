package org.dhis2.ruleengine

class RuleEngineFunctionTest {
   /* private val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.US)

    @Test
    @Throws(Exception::class)
    fun evaluateFailingRule() {
        val enrollmentDate = currentDate()
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "2 + 2"
        )
        val failingRule = Rule(
            "",
            null,
            null,
            "d2:daysBetween(V{enrollment_date},V{event_date}) < 0",
            listOf(ruleAction),
            ""
        )
        val validRule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleEngine: RuleEngine = getRuleEngine(listOf(failingRule, validRule))
        val ruleEnrollment: RuleEnrollment = RuleEnrollment(
            "test_enrollment",
            "test_enrollment",
            enrollmentDate, enrollmentDate, RuleEnrollment.Status.ACTIVE, "", null,
            listOf()
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEnrollment = ruleEnrollment)
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("4")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateFailingRuleInMultipleContext() {
        val today = currentDate()
        val yesterday = LocalDate.now().minusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "2 + 2"
        )
        val failingRule = Rule(
            "", null, null, "d2:daysBetween(V{enrollment_date},V{event_date}) < 0",
            listOf(ruleAction), ""
        )
        val ruleEnrollment: RuleEnrollment = RuleEnrollment(
            "test_enrollment",
            "",
            today, today, RuleEnrollment.Status.ACTIVE, "", null,
            listOf<RuleAttributeValue>(),
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage", "",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(),null, "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleNotFailingEvent: RuleEvent = RuleEvent(
            "test_not_failing_event", "test_program_stage","",
            RuleEvent.Status.ACTIVE, yesterday, yesterday,null, "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            )
        )
        val ruleEngine: RuleEngine = getRuleEngine(
            rules = listOf(failingRule), ruleEnrollment = ruleEnrollment,
            ruleEvents = listOf(ruleEvent, ruleNotFailingEvent)
        )
        val RuleEffect = ruleEngine.evaluate()
        assertThat(RuleEffect.size).isEqualTo(3)
        assertThat(getRuleEffectByUid(RuleEffect, "test_event").getRuleEffect()).isEmpty()
        assertThat(getRuleEffectByUid(RuleEffect, "test_not_failing_event").getRuleEffect()).isNotEmpty()
        assertThat(getRuleEffectByUid(RuleEffect, "test_not_failing_event").getRuleEffect().get(0).data)
            .isEqualTo("4")
        assertThat(getRuleEffectByUid(RuleEffect, "test_enrollment").getRuleEffect()).isNotEmpty()
    }

    private fun getRuleEffectByUid(RuleEffect: List<RuleEffect>, uid: String): RuleEffect? {
        for (ruleEffect in RuleEffect) {
            if (ruleEffect.getTrackerObjectUid().equals(uid)) {
                return ruleEffect
            }
        }
        return null
    }

    @Test
    @Throws(Exception::class)
    fun evaluateHasValueFunctionMustReturnTrueIfValueSpecified() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:hasValue(#{test_variable})"
        )
        val ruleVariable: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable", "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(
                    currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("true")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateHasValueFunctionWithStringValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:hasValue('test_variable')"
        )
        val ruleVariable: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable", "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(
                    currentDate(), "test_program_stage", "test_data_element", "test_value"
                )
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("true")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateHasValueFunctionMustReturnTrueIfNoValueSpecified() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:hasValue(#{test_variable})"
        )
        val ruleVariable: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_variable", "test_data_element", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, ArrayList<RuleDataValue>(),
            "test_program_stage_name", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("false")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateEnvironmentVariableProgramStageName() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "V{program_stage_name}"
        )
        val ruleVariable: RuleVariable = RuleVariable.RuleVariableCurrentEvent
        ("variable", "test_data_element", RuleValueType.TEXT)
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariable))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage_id",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, ArrayList<RuleDataValue>(),
            "test_program_stage_name", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("test_program_stage_name")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateDaysBetweenMustReturnCorrectDiff() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:daysBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue
                    (currentDate(), "test_program_stage", "test_data_element_two", "2017-02-01")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("31")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateDaysBetweenWithSingleQuotedDateMustReturnCorrectDiff() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:daysBetween(#{test_var_one}, '2018-01-01')"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue
                    (currentDate(), "test_program_stage", "test_data_element_two", "2017-02-01")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("365")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2InOrgUnitGroup() {
        val members = listOf("location1", "location2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["OU_GROUP_ID"] = members
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:inOrgUnitGroup(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = RuleEngineContext
            .builder()
            .rules(listOf(rule))
            .ruleVariables(listOf(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap<String, String>())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "location1", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")
            ),
            "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("true")
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateD2InOrgUnitGroupWithStringValue() {
        val members = listOf("location1", "location2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["OU_GROUP_ID"] = members
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:inOrgUnitGroup('OU_GROUP_ID')"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = RuleEngineContext
            .builder()
            .rules(listOf(rule))
            .ruleVariables(listOf(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap<String, String>())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "location1", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "OU_GROUP_ID")
            ),
            "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("true")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2HasUserRole() {
        val roles = listOf("role1", "role2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["USER"] = roles
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:hasUserRole(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = RuleEngineContext
            .builder()
            .rules(listOf(rule))
            .ruleVariables(listOf(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap<String, String>())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "location1", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "role1")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("true")
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateD2HasUserRoleWithStringValue() {
        val roles = listOf("role1", "role2")
        val supplementaryData: MutableMap<String, List<String>> = HashMap()
        supplementaryData["USER"] = roles
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:hasUserRole('role1')"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = RuleEngineContext
            .builder()
            .rules(listOf(rule))
            .ruleVariables(listOf(ruleVariableOne))
            .supplementaryData(supplementaryData)
            .constantsValue(HashMap<String, String>())
            .build().toEngineBuilder().triggerEnvironment(TriggerEnvironment.SERVER)
            .build()
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "location1", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "role1")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("true")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2AddDays() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:addDays(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(rule, listOf(ruleVariableOne, ruleVariableTwo))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2017-01-01"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "2")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("2017-01-03")
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2017-01-03"),
                RuleDataValue
                    (currentDate(), "test_program_stage", "test_data_element_two", "-2")
            ), "", null
        )
        val RuleEffect2: List<RuleEffect> = ruleEngine.evaluate(ruleEvent2).call()
        Assertions.assertThat(RuleEffect2.size).isEqualTo(1)
        assertThat(RuleEffect2[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect2[0].data).isEqualTo("2017-01-01")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:countIfValue(#{test_var_one}, 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(listOf(ruleEvent2, ruleEvent3))
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertTrue(RuleEffect[0].data.equals("2"))
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateD2CountIfValueWithStringValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:countIfValue('test_var_one', 'condition')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(listOf(ruleEvent2, ruleEvent3))
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        Assert.assertTrue(RuleEffect[0].data.equals("2"))
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Count() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent4: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(listOf(ruleEvent2, ruleEvent3, ruleEvent4))
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("3", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateLogicalAnd() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:hasValue(V{current_date}) && d2:count(#{test_var_one}) > 0",
            listOf(ruleAction), "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        ruleEngineBuilder.events(listOf(ruleEvent2))
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateLogicalOr() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:count(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:hasValue(V{current_date}) || d2:count(#{test_var_one}) > 0",
            listOf(ruleAction), "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        ruleEngineBuilder.events(listOf(ruleEvent2))
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", RuleEffect[0].data)
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateD2CountWithStringValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:count('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition2")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "condition")
            ), "", null
        )
        val ruleEvent4: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "condition")
            ), "", null
        )
        ruleEngineBuilder.events(listOf(ruleEvent2, ruleEvent3, ruleEvent4))
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("3", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Round() {
        val ruleAction1: RuleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:round(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule(null, null, "true", listOf(ruleAction1), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2.6")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction1)
        assertEquals("3", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Modulus() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:modulus(#{test_var_one}, 2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2.6")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("0.6", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2SubString() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:substring(#{test_var_one}, 1, 3)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "ABCD")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("BC", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2WeeksBetween() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:weeksBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "2018-02-01")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("4", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2MonthsBetween() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:monthsBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2018-01-01"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "2018-09-01")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("8", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2YearsBetween() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:yearsBetween(#{test_var_one}, #{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:yearsBetween('2016-01-01', '2018-09-01') == 2", listOf(ruleAction), "",
            ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "2016-01-01"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "2018-09-01")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Zpvc() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:zpvc( '1', '0', '-1' )"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.NUMERIC
        )
        val ruleVariableThree: RuleVariable = RuleVariableNewestEvent(
            "test_var_three", "test_data_element_two", RuleValueType.NUMERIC
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf<RuleDataValue>(), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Zing() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:zing( '-1' )"
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf<RuleVariable>())
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf<RuleDataValue>(), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("0", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Oizp() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:oizp( '0' )"
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf<RuleVariable>())
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf<RuleDataValue>(), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("1", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2CountIfZeroPos() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "0")
            ), "", null
        )
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "1")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "-3")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", RuleEffect[0].data)
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateD2CountIfZeroPosWithStringValue() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "0")
            ), "", null
        )
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "1")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "-3")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("2", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Left() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:left(#{test_var_one}, 4)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("yyyy", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Right() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:right(#{test_var_one}, 2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "yyyy-mm-dd")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("dd", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Concatenate() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:concatenate(#{test_var_one}, '+days')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "weeks")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("weeks+days", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ValidatePattern() {
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:validatePattern(#{test_var_one}, '.*555.*')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "44455545454")
            ),
            "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("true", RuleEffect[0].data)
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "444887777")
            ), "", null
        )
        val RuleEffect2: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent2).call()
        Assertions.assertThat(RuleEffect2.size).isEqualTo(1)
        assertThat(RuleEffect2[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("false", RuleEffect2[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Length() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:length(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "testString")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("10", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2Split() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:split(#{test_var_one},'-',2)"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue
                    (currentDate(), "test_program_stage", "test_data_element_one", "test-String-for-split")
            ),
            "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals("for", RuleEffect[0].data)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateNestedFunctionCalls() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:floor(#{test_var_one} + d2:ceil(#{test_var_three})) " +
                    "/ 5 * d2:ceil(#{test_var_two})"
        )
        val ruleVariableOne: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_two", "test_data_element_two", RuleValueType.NUMERIC
        )
        val ruleVariableThree: RuleVariable = RuleVariable.RuleVariableCurrentEvent(
            "test_var_three", "test_data_element_three", RuleValueType.NUMERIC
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngine: RuleEngine = getRuleEngine(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo, ruleVariableThree)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "19.9"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "0.9"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_three", "10.6")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngine.evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].data).isEqualTo("6")
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreWFA() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:zScoreWFA(1,#{test_var_one},#{test_var_two}) == 0", listOf(ruleAction),
            "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "4.5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreHFAGirl() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:zScoreHFA(12,#{test_var_one},#{test_var_two}) == -3", listOf(ruleAction),
            "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "66.3"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "1")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreHFABoy() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:zScoreHFA(10,#{test_var_one},#{test_var_two}) == -2", listOf(ruleAction),
            "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "68.7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreWFHBoy() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:zScoreWFH(52,#{test_var_one},A{test_var_two}) < 2", listOf(ruleAction),
            "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "3"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2ZScoreWFHGirl() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:zScoreWFH(81.5,9.6,'female') == 2"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(
            null, null, "d2:zScoreWFH(81.5,#{test_var_one},#{test_var_two}) == 2", listOf(ruleAction),
            "", ""
        )
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent: RuleEvent = RuleEvent(
            "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "12.5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "1")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.build().evaluate(ruleEvent).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun evaluateD2MaxValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "d2:maxValue(#{test_var_one}) == 8.0", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Deprecated("")
    @Throws(Exception::class)
    fun evaluateD2MaxValueWithStringValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "true"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "d2:maxValue('test_var_one') == 8.0", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
    }

    @Test
    @Throws(Exception::class)
    fun testMinValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:minValue(#{test_var_one})"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo: RuleVariable = RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("5.0")
    }

    @Test
    @Throws(Exception::class)
    fun testMinValueWithStringValue() {
        val ruleAction = RuleAction.DisplayKeyValuePair(
            "test_action_content",
            DisplayLocation.LOCATION_FEEDBACK_WIDGET,
            "d2:minValue('test_var_one')"
        )
        val ruleVariableOne = RuleVariable.RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.NUMERIC
        )
        val ruleVariableTwo = RuleVariable.RuleVariableNewestEvent(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT
        )
        val rule = Rule("", null, null, "true", listOf(ruleAction), "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(
            rule,
            listOf(ruleVariableOne, ruleVariableTwo)
        )
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "5"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "7"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, currentDate(), currentDate(), "", null, listOf(
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_one", "8"),
                RuleDataValue(currentDate(), "test_program_stage", "test_data_element_two", "male")
            ), "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertThat(RuleEffect[0].data).isEqualTo("5.0")
    }

    @Test
    @Throws(Exception::class)
    fun evaluateLastEventcurrentDate() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val yesterday = cal.time
        cal.add(Calendar.DATE, -1)
        val dayBeforeYesterday = cal.time
        cal.add(Calendar.DATE, 4)
        val dayAfterTomorrow = cal.time
        val ruleAction: RuleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:lastEventDate('test_var_one')"
        )
        val ruleVariableOne: RuleVariable = RuleVariableNewestEvent(
            "test_var_one", "test_data_element_one", RuleValueType.TEXT
        )
        val rule = Rule(null, null, "true", listOf(ruleAction), "test_rule", "")
        val ruleEngineBuilder: RuleEngine.Builder = getRuleEngineBuilder(rule, listOf(ruleVariableOne))
        val ruleEvent1: RuleEvent = RuleEvent(
            "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, dayBeforeYesterday, currentDate(), "", null, listOf(
                RuleDataValue(dayBeforeYesterday, "test_program_stage1", "test_data_element_one", "value1")
            ),
            "", null
        )
        val ruleEvent2: RuleEvent = RuleEvent(
            "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, yesterday, currentDate(), "", null, listOf(
                RuleDataValue(yesterday, "test_program_stage2", "test_data_element_one", "value2")
            ), "", null
        )
        val ruleEvent3: RuleEvent = RuleEvent(
            "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, dayAfterTomorrow, dayAfterTomorrow, "", null, listOf(
                RuleDataValue(dayAfterTomorrow, "test_program_stage3", "test_data_element_one", "value3")
            ),
            "", null
        )
        val RuleEffect: List<RuleEffect> = ruleEngineBuilder.events(listOf(ruleEvent1, ruleEvent2)).build()
            .evaluate(ruleEvent3).call()
        assertThat(RuleEffect.size).isEqualTo(1)
        assertThat(RuleEffect[0].ruleAction).isEqualTo(ruleAction)
        assertEquals(dateFormat.format(dayAfterTomorrow), RuleEffect[0].data)
    }

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }*/
}