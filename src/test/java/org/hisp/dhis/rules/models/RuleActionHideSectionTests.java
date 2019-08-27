package org.hisp.dhis.rules.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hisp.dhis.rules.models.RuleActionHideSection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.fail;

@RunWith( JUnit4.class )
public class RuleActionHideSectionTests
{

        @Test
        public void createMustThrowOnNullField()
        {
                try
                {
                        RuleActionHideSection.create( null );
                        fail( "NullPointerException was expected, but nothing was thrown." );
                }
                catch ( Exception nullPointerException )
                {
                        // noop
                }
        }

        @Test
        public void equalsAndHashCodeFunctionsMustConformContract()
        {
                EqualsVerifier.forClass( RuleActionHideSection.create( "test_field" ).getClass() )
                    .suppress( Warning.NULL_FIELDS )
                    .verify();
        }
}
