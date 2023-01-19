package org.dhis2.ruleengine.gs1

/*
 * Copyright (c) 2004-2018, University of Oslo
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
 */   enum class GS1Elements(val element: String) {
    GS1_E0_IDENTIFIER("]E0"),  // EAN-13, UPC-A, UPC-E
    GS1_E1_IDENTIFIER("]E1"),  // Two-digit add-on symbol
    GS1_E2_IDENTIFIER("]E2"),  // Five-digit add-on symbol
    GS1_E3_IDENTIFIER("]E3"),  // EAN-13, UPC-A, UPC-E with add-on symbol
    GS1_E4_IDENTIFIER("]E4"),  // EAN-8
    GS1_I1_IDENTIFIER("]I1"),  // ITF-14
    GS1_C1_IDENTIFIER("]C1"),  // GS1-128
    GS1_e0_IDENTIFIER("]e0"),  // GS1 DataBar
    GS1_e1_IDENTIFIER("]e1"),  // GS1 Composite
    GS1_e2_IDENTIFIER("]e2"),  // GS1 Composite
    GS1_d2_IDENTIFIER("]d2"),  // GS1 DataMatrix
    GS1_Q3_IDENTIFIER("]Q3"),  // GS1 QR Code
    GS1_J1_IDENTIFIER("]J1"),  // GS1 DotCode
    GS1_d1_IDENTIFIER("]d1"),  // Data Matrix implementing ECC 200
    GS1_Q1_IDENTIFIER("]Q1"),  // QR Code
    GS1_GROUP_SEPARATOR("\u001d"), SSCC("00"), GTIN("01"), CONTENT("02"), LOT_NUMBER("10"), PROD_DATE("11"), DUE_DATE("12"), PACK_DATE(
        "13"
    ),
    BEST_BEFORE_DATE("15"), SELL_BY("16"), EXP_DATE("17"), VARIANT("20"), SERIAL_NUMBER("21"), CPV("22"), TPX("235"), ADDITIONAL_ID(
        "240"
    ),
    CUSTOMER_PART_NUMBER("241"), MTO_VARIANT_NUMBER("242"), PCN("243"), SECONDARY_SERIAL("250"), REF_TO_SOURCE("251"), GDTI(
        "253"
    ),
    GLN_EXTENSION_COMPONENT("254"), GCN("255"), VAR_COUNT("30"), NET_WEIGHT_KG("310*"), LENGTH_M("311*"), WIDTH_M("312*"), HEIGHT_M(
        "313*"
    ),
    AREA_M2("314*"), NET_VOLUME_L("315*"), NET_VOLUME_M3("316*"), NET_WEIGHT_LB("320*"), LENGTH_I("321*"), LENGTH_F("322*"), LENGTH_Y(
        "323*"
    ),
    WIDTH_I("324*"), WIDTH_F("325*"), WIDTH_Y("326*"), HEIGHT_I("327*"), HEIGHT_F("328*"), HEIGHT_Y("329*"), GROSS_WEIGHT_GF(
        "330*"
    ),
    LENGTH_M_LOG("331*"), WIDTH_M_LOG("332*"), HEIGHT_M_LOG("333*"), AREA_M2_LOG("334*"), VOLUME_L_LOG("335*"), VOLUME_M3_LOG(
        "336*"
    ),
    KG_PER_M2("337*"), GROSS_WHEIGHT_LB("340*"), LENGTH_I_LOG("341*"), LENGTH_F_LOG("342*"), LENGTH_Y_LOG("343*"), WIDTH_I_LOG(
        "344*"
    ),
    WIDTH_F_LOG("345*"), WIDTH_Y_LOG("346*"), HEIGHT_I_LOG("347*"), HEIGHT_F_LOG("348*"), HEIGHT_Y_LOG("349*"), AREA_I2(
        "350*"
    ),
    AREA_F2("351*"), AREA_Y2("352*"), AREA_I2_LOG("353*"), AREA_F2_LOG("354*"), AREA_Y2_LOG("355*"), NET_WEIGHT_T("356*"), NET_VOLUME_OZ(
        "357*"
    ),
    NET_VOLUME_Q("360*"), NET_VOLUME_G("361*"), VOLUME_Q_LOG("362*"), VOLUME_G_LOG("363*"), VOLUME_I3("364*"), VOLUME_F3(
        "365*"
    ),
    VOLUME_Y3("366*"), VOLUME_I3_LOG("367*"), VOLUME_F3_LOG("368*"), VOLUME_Y3_LOG("369*"), COUNT("37"), AMOUNT("390*"), AMOUNT_ISO(
        "391*"
    ),
    PRICE("392*"), PRICE_ISO("393*"), PRCNT_OFF("394*"), PRICE_UOM("395*"), ORDER_NUMBER("400"), GINC("401"), ROUTE("403"), SHIP_TO_GLOB_LOC(
        "410"
    ),
    BILL_TO_LOC("411"), PURCHASED_FROM("412"), SHIP_FOR_LOG("413"), LOC_NUMBER("414"), PAY_TO("415"), PROD_SERV_LOC("416"), PARTY(
        "417"
    ),
    SHIP_TO_POST("420"), SHIP_TO_POST_ISO("421"), ORIGIN("422"), COUNTRY_INITIAL_PROCESS("423"), COUNTRY_PROCESS("424"), COUNTRY_DISASSEMBLY(
        "425"
    ),
    COUNTRY_FULL_PROCESS("426"), ORIGIN_SUBDIVISION("427"), SHIP_TO_COMP("4300"), SHIP_TO_NAME("4301"), SHIP_TO_ADD1("4302"), SHIP_TO_ADD2(
        "4303"
    ),
    SHIP_TO_SUB("4304"), SHIP_TO_LOCALITY("4305"), SHIP_TO_REG("4306"), SHIP_TO_COUNTRY("4307"), SHIP_TO_PHONE("4308"), RTN_TO_COMP(
        "4310"
    ),
    RTN_TO_NAME("4311"), RTN_TO_ADD1("4312"), RTN_TO_ADD2("4313"), RTN_TO_SUB("4314"), RTN_TO_LOCALITY("4315"), RTN_TO_REG(
        "4316"
    ),
    RTN_TO_COUNTRY("4317"), RTN_TO_POST("4318"), RTN_TO_PHONE("4319"), SRV_DESCRIPTION("4320"), DANGEROUS_GOODS("4321"), AUTH_LEAV(
        "4322"
    ),
    SIG_REQUIRED("4323"), NBEF_DEL_DT("4324"), NAFT_DEL_DT("4325"), REL_DATE("4326"), NSN("7001"), MEAT_CUT("7002"), EXP_TIME(
        "7003"
    ),
    ACTIVE_POTENCY("7004"), CATCH_AREA("7005"), FIRST_FREEZE_DATE("7006"), HARVEST_DATE("7007"), AQUATIC_SPECIES("7008"), FISHING_GEAR_TYPE(
        "7009"
    ),
    PROD_METHID("7010"), REFURB_LOT("7020"), FUNC_STAT("7021"), REV_STAT("7022"), GIAI_ASSEMBLY("7023"), PROCESSOR_NUMBER(
        "703*"
    ),
    UIC_EXT("7040"), NHRN_PZN("710"), NHRN_CIP("711"), NHRN_CN("712"), NHRN_DRN("713"), NHRN_AIM("714"), CERT_NUMBER("723*"), PROTOCOL(
        "7240"
    ),
    DIMENSIONS("8001"), CMT_NUMBER("8002"), GRAI("8003"), GIAI("8004"), PRICE_PER_UNIT("8005"), ITIP("8006"), IBAN("8007"), PROD_TIME(
        "8008"
    ),
    OPTSEN("8009"), CPID("8010"), CPID_SERIAL("8011"), VERSION("8012"), GMN("8013"), GSRN_PROVIDER("8017"), GSRN_RECIPIENT(
        "8018"
    ),
    SRIN("8019"), REF_NUMBER("8020"), ITIP_CONTENT("8026"), COUPON_USA("8110"), POINTS("8111"), POSITIVE_OFFER_COUPON_USA(
        "8121"
    ),
    PRODUCT_URL("8200"), AGREEMENT_INTERNAL("90"), COMPANY_INTERNAL_1("91"), COMPANY_INTERNAL_2("92"), COMPANY_INTERNAL_3(
        "93"
    ),
    COMPANY_INTERNAL_4("94"), COMPANY_INTERNAL_5("95"), COMPANY_INTERNAL_6("96"), COMPANY_INTERNAL_7("97"), COMPANY_INTERNAL_8(
        "98"
    ),
    COMPANY_INTERNAL_9("99");

    companion object {
        val GS1Identifiers: Set<String> = setOf(
            GS1_E0_IDENTIFIER.element,
            GS1_E1_IDENTIFIER.element,
            GS1_E2_IDENTIFIER.element,
            GS1_E3_IDENTIFIER.element,
            GS1_E4_IDENTIFIER.element,
            GS1_I1_IDENTIFIER.element,
            GS1_C1_IDENTIFIER.element,
            GS1_e0_IDENTIFIER.element,
            GS1_e1_IDENTIFIER.element,
            GS1_e2_IDENTIFIER.element,
            GS1_d2_IDENTIFIER.element,
            GS1_Q3_IDENTIFIER.element,
            GS1_J1_IDENTIFIER.element,
            GS1_d1_IDENTIFIER.element,
            GS1_Q1_IDENTIFIER.element
        )

        fun fromKey(key: String): GS1Elements {
            return when (key) {
                "gtin" -> GTIN
                "lot number", "batch number" -> LOT_NUMBER
                "production date" -> PROD_DATE
                "best before date" -> BEST_BEFORE_DATE
                "expiration date" -> EXP_DATE
                "serial number" -> SERIAL_NUMBER
                else -> {
                    for (gs1Elements in values()) {
                        if (gs1Elements.element == key) {
                            return gs1Elements
                        }
                    }
                    throw IllegalArgumentException("invalid or not supported gs1 key")
                }
            }
        }

        fun getApplicationIdentifier(gs1Group: String): String {
            for (gs1Elements in values()) {
                var ai = gs1Elements.element
                if (ai.endsWith("*")) {
                    ai = ai.substring(0, ai.length - 1)
                }
                if (gs1Group.startsWith(ai) && ai.endsWith("*")) {
                    return gs1Group.substring(0, ai.length + 1)
                } else if (gs1Group.startsWith(ai)) {
                    return ai
                }
            }
            throw IllegalArgumentException("Could not retrieve ai from value")
        }
    }
}