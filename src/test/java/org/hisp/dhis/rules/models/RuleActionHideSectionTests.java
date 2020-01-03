package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith( JUnit4.class )
public class RuleActionHideSectionTests
{

        @Test(expected = NullPointerException.class )
        public void createMustThrowOnNullField()
        {
                RuleActionHideSection.create( null );
        }

        @Test
        public void equalsAndHashCodeFunctionsMustConformContract()
        {
                EqualsVerifier.forClass( RuleActionHideSection.create( "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
