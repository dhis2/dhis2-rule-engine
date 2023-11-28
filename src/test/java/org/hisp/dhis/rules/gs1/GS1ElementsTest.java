package org.hisp.dhis.rules.gs1;

import org.hisp.dhis.lib.expression.math.GS1Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class GS1ElementsTest {
    @Test
    public void shouldReturnGS1Element(){
        assertEquals( GS1Elements.GTIN , GS1Elements.fromKey("gtin") );
        assertEquals( GS1Elements.GTIN , GS1Elements.fromKey("GTIN") );
        assertEquals( GS1Elements.LOT_NUMBER , GS1Elements.fromKey("lot number") );
        assertEquals( GS1Elements.LOT_NUMBER , GS1Elements.fromKey("LOT_NUMBER") );
        assertEquals( GS1Elements.LOT_NUMBER , GS1Elements.fromKey("LOT NUMBER") );
        assertEquals( GS1Elements.AREA_I2 , GS1Elements.fromKey("AREA I2") );
        assertEquals( GS1Elements.PAY_TO , GS1Elements.fromKey("PAY_TO") );
        assertEquals( GS1Elements.PAY_TO , GS1Elements.fromKey("PAY TO") );
        assertEquals( GS1Elements.PAY_TO , GS1Elements.fromKey("pay to") );
        assertEquals( GS1Elements.AREA_I2 , GS1Elements.fromKey("AREA I2") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("product url") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("product_url") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("productUrl") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("ProductUrl") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("Product_Url") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("PRODUCT_URL") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("PRODUCT URL") );
        assertEquals( GS1Elements.PRODUCT_URL , GS1Elements.fromKey("PRODUCTURL") );
    }
}
