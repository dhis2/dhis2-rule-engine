package org.hisp.dhis.rules.gs1;

import org.hisp.dhis.lib.expression.math.GS1Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class GS1ElementsTest {
    @Test
    public void shouldReturnGS1Element(){
        assertThat( GS1Elements.fromKey("gtin") ).isEqualTo( GS1Elements.GTIN );
        assertThat( GS1Elements.fromKey("GTIN") ).isEqualTo( GS1Elements.GTIN );
        assertThat( GS1Elements.fromKey("lot number") ).isEqualTo( GS1Elements.LOT_NUMBER );
        assertThat( GS1Elements.fromKey("LOT_NUMBER") ).isEqualTo( GS1Elements.LOT_NUMBER );
        assertThat( GS1Elements.fromKey("LOT NUMBER") ).isEqualTo( GS1Elements.LOT_NUMBER );
        assertThat( GS1Elements.fromKey("AREA I2") ).isEqualTo( GS1Elements.AREA_I2 );
        assertThat( GS1Elements.fromKey("PAY_TO") ).isEqualTo( GS1Elements.PAY_TO );
        assertThat( GS1Elements.fromKey("PAY TO") ).isEqualTo( GS1Elements.PAY_TO );
        assertThat( GS1Elements.fromKey("pay to") ).isEqualTo( GS1Elements.PAY_TO );
        assertThat( GS1Elements.fromKey("AREA I2") ).isEqualTo( GS1Elements.AREA_I2 );
        assertThat( GS1Elements.fromKey("product url") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("product_url") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("productUrl") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("ProductUrl") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("Product_Url") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("PRODUCT_URL") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("PRODUCT URL") ).isEqualTo( GS1Elements.PRODUCT_URL );
        assertThat( GS1Elements.fromKey("PRODUCTURL") ).isEqualTo( GS1Elements.PRODUCT_URL );
    }
}
