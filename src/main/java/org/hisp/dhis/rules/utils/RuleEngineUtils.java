package org.hisp.dhis.rules.utils;

/*
 * Copyright (c) 2004-2020, University of Oslo
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

import org.hisp.dhis.rules.ItemValueType;

import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Zubair Asghar
 */
public class RuleEngineUtils
{
    public static final String ENV_VAR_CURRENT_DATE = "current_date";
    public static final String ENV_VAR_COMPLETED_DATE = "completed_date";
    public static final String ENV_VAR_EVENT_DATE = "event_date";
    public static final String ENV_VAR_EVENT_COUNT = "event_count";
    public static final String ENV_VAR_DUE_DATE = "due_date";
    public static final String ENV_VAR_EVENT_ID = "event_id";
    public static final String ENV_VAR_ENROLLMENT_DATE = "enrollment_date";
    public static final String ENV_VAR_ENROLLMENT_ID = "enrollment_id";
    public static final String ENV_VAR_ENROLLMENT_COUNT = "enrollment_count";
    public static final String ENV_VAR_INCIDENT_DATE = "incident_date";
    public static final String ENV_VAR_TEI_COUNT = "tei_count";
    public static final String ENV_VAR_EVENT_STATUS = "event_status";
    public static final String ENV_VAR_OU = "org_unit";
    public static final String ENV_VAR_ENROLLMENT_STATUS = "enrollment_status";
    public static final String ENV_VAR_PROGRAM_STAGE_ID = "program_stage_id";
    public static final String ENV_VAR_PROGRAM_STAGE_NAME = "program_stage_name";
    public static final String ENV_VAR_PROGRAM_NAME = "program_name";
    public static final String ENV_VAR_ENVIRONMENT = "environment";
    public static final String ENV_VAR_OU_CODE = "orgunit_code";

    // new environment variable must be added in this map
    public static final Map<String, ItemValueType> ENV_VARIABLES = Map.ofEntries(
            entry( ENV_VAR_COMPLETED_DATE, ItemValueType.DATE ),
            entry( ENV_VAR_CURRENT_DATE, ItemValueType.DATE ),
            entry( ENV_VAR_EVENT_DATE, ItemValueType.DATE ),
            entry( ENV_VAR_INCIDENT_DATE, ItemValueType.DATE ),
            entry( ENV_VAR_ENROLLMENT_DATE, ItemValueType.DATE ),
            entry( ENV_VAR_DUE_DATE, ItemValueType.DATE ),
            entry( ENV_VAR_EVENT_COUNT, ItemValueType.NUMBER ),
            entry( ENV_VAR_TEI_COUNT, ItemValueType.NUMBER ),
            entry( ENV_VAR_ENROLLMENT_COUNT, ItemValueType.NUMBER ),
            entry( ENV_VAR_EVENT_ID, ItemValueType.NUMBER ),
            entry( ENV_VAR_PROGRAM_STAGE_ID, ItemValueType.NUMBER ),
            entry( ENV_VAR_ENROLLMENT_ID, ItemValueType.NUMBER ),
            entry( ENV_VAR_ENROLLMENT_STATUS, ItemValueType.TEXT ),
            entry( ENV_VAR_EVENT_STATUS, ItemValueType.TEXT ),
            entry( ENV_VAR_OU, ItemValueType.TEXT ),
            entry( ENV_VAR_OU_CODE, ItemValueType.TEXT ),
            entry( ENV_VAR_ENVIRONMENT, ItemValueType.TEXT ),
            entry( ENV_VAR_PROGRAM_NAME, ItemValueType.TEXT ),
            entry( ENV_VAR_PROGRAM_STAGE_NAME, ItemValueType.TEXT )
            );
}
