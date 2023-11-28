package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith( JUnit4.class )
public class RuleDataValueTest
{
    @Test
    public void createShouldPropagateValuesCorrectly()
    {
        Date eventDate = new Date();
        RuleDataValue ruleDataValue = RuleDataValue.create( eventDate,
            "test_program_stage_uid", "test_dataelement", "test_value" );

        assertEquals( eventDate , ruleDataValue.eventDate() );
        assertEquals( "test_program_stage_uid" , ruleDataValue.programStage() );
        assertEquals( "test_dataelement" , ruleDataValue.dataElement() );
        assertEquals( "test_value" , ruleDataValue.value() );
    }
}
