package org.hisp.dhis.rules.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith( JUnit4.class )
public class RuleDataValueTest
{
    @Test
    public void createShouldPropagateValuesCorrectly()
    {
        Date eventDate = new Date();
        RuleDataValue ruleDataValue = RuleDataValue.create( eventDate,
            "test_program_stage_uid", "test_dataelement", "test_value" );

        assertThat( ruleDataValue.eventDate() ).isEqualTo( eventDate );
        assertThat( ruleDataValue.programStage() ).isEqualTo( "test_program_stage_uid" );
        assertThat( ruleDataValue.dataElement() ).isEqualTo( "test_dataelement" );
        assertThat( ruleDataValue.value() ).isEqualTo( "test_value" );
    }
}
