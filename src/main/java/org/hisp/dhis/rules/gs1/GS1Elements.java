package org.hisp.dhis.rules.gs1;

public enum GS1Elements {
    GS1_DATA_MATRIX_IDENTIFIER("]d2"),
    GS1_GROUP_SEPARATOR("\u001d"),
    SSCC("00"),
    GTIN("01"),
    CONTENT("02"),
    LOT_NUMBER("10"),
    PROD_DATE("11"),
    DUE_DATE("12"),
    PACK_DATE("13"),
    BEST_BEFORE_DATE("15"),
    SELL_BY("16"),
    EXP_DATE("17"),
    VARIANT("20"),
    SERIAL_NUMBER("21"),
    CPV("22"),
    TPX("235"),
    ADDITIONAL_ID("240"),
    CUSTOMER_PART_NUMBER("241"),
    MTO_VARIANT_NUMBER("242"),
    PCN("243"),
    SECONDARY_SERIAL("250"),
    REF_TO_SOURCE("251"),
    GDTI("253"),
    GLN_EXTENSION_COMPONENT("254"),
    GCN("255"),
    VAR_COUNT("30"),
    NET_WEIGHT_KG("310*"),
    LENGTH_M("311*"),
    WIDTH_M("312*"),
    HEIGHT_M("313*"),
    AREA_M2("314*"),
    NET_VOLUME_L("315*"),
    NET_VOLUME_M3("316*"),
    NET_WEIGHT_LB("320*"),
    LENGTH_I("321*"),
    LENGTH_F("322*"),
    LENGTH_Y("323*"),
    WIDTH_I("324*"),
    WIDTH_F("325*"),
    WIDTH_Y("326*"),
    HEIGHT_I("327*"),
    HEIGHT_F("328*"),
    HEIGHT_Y("329*"),
    GROSS_WEIGHT_GF("330*"),
    LENGTH_M_LOG("331*"),
    WIDTH_M_LOG("332*"),
    HEIGHT_M_LOG("333*"),
    AREA_M2_LOG("334*"),
    VOLUME_L_LOG("335*"),
    VOLUME_M3_LOG("336*"),
    KG_PER_M2("337*"),
    GROSS_WHEIGHT_LB("340*"),
    LENGTH_I_LOG("341*"),
    LENGTH_F_LOG("342*"),
    LENGTH_Y_LOG("343*"),
    WIDTH_I_LOG("344*"),
    WIDTH_F_LOG("345*"),
    WIDTH_Y_LOG("346*"),
    HEIGHT_I_LOG("347*"),
    HEIGHT_F_LOG("348*"),
    HEIGHT_Y_LOG("349*"),
    AREA_I2("350*"),
    AREA_F2("351*"),
    AREA_Y2("352*"),
    AREA_I2_LOG("353*"),
    AREA_F2_LOG("354*"),
    AREA_Y2_LOG("355*"),
    NET_WEIGHT_T("356*"),
    NET_VOLUME_OZ("357*"),
    NET_VOLUME_Q("360*"),
    NET_VOLUME_G("361*"),
    VOLUME_Q_LOG("362*"),
    VOLUME_G_LOG("363*"),
    VOLUME_I3("364*"),
    VOLUME_F3("365*"),
    VOLUME_Y3("366*"),
    VOLUME_I3_LOG("367*"),
    VOLUME_F3_LOG("368*"),
    VOLUME_Y3_LOG("369*"),
    COUNT("37"),
    AMOUNT("390*"),
    AMOUNT_ISO("391*"),
    PRICE("392*"),
    PRICE_ISO("393*"),
    PRCNT_OFF("394*"),
    PRICE_UOM("395*"),
    ORDER_NUMBER("400"),
    GINC("401"),
    ROUTE("403"),
    SHIP_TO_GLOB_LOC("410"),
    BILL_TO_LOC("411"),
    PURCHASED_FROM("412"),
    SHIP_FOR_LOG("413"),
    LOC_NUMBER("414"),
    PAY_TO("415"),
    PROD_SERV_LOC("416"),
    PARTY("417"),
    SHIP_TO_POST("420"),
    SHIP_TO_POST_ISO("421"),
    ORIGIN("422"),
    COUNTRY_INITIAL_PROCESS("423"),
    COUNTRY_PROCESS("424"),
    COUNTRY_DISASSEMBLY("425"),
    COUNTRY_FULL_PROCESS("426"),
    ORIGIN_SUBDIVISION("427"),
    SHIP_TO_COMP("4300"),
    SHIP_TO_NAME("4301"),
    SHIP_TO_ADD1("4302"),
    SHIP_TO_ADD2("4303"),
    SHIP_TO_SUB("4304"),
    SHIP_TO_LOCALITY("4305"),
    SHIP_TO_REG("4306"),
    SHIP_TO_COUNTRY("4307"),
    SHIP_TO_PHONE("4308"),
    RTN_TO_COMP("4310"),
    RTN_TO_NAME("4311"),
    RTN_TO_ADD1("4312"),
    RTN_TO_ADD2("4313"),
    RTN_TO_SUB("4314"),
    RTN_TO_LOCALITY("4315"),
    RTN_TO_REG("4316"),
    RTN_TO_COUNTRY("4317"),
    RTN_TO_POST("4318"),
    RTN_TO_PHONE("4319"),
    SRV_DESCRIPTION("4320"),
    DANGEROUS_GOODS("4321"),
    AUTH_LEAV("4322"),
    SIG_REQUIRED("4323"),
    NBEF_DEL_DT("4324"),
    NAFT_DEL_DT("4325"),
    REL_DATE("4326"),
    NSN("7001"),
    MEAT_CUT("7002"),
    EXP_TIME("7003"),
    ACTIVE_POTENCY("7004"),
    CATCH_AREA("7005"),
    FIRST_FREEZE_DATE("7006"),
    HARVEST_DATE("7007"),
    AQUATIC_SPECIES("7008"),
    FISHING_GEAR_TYPE("7009"),
    PROD_METHID("7010"),
    REFURB_LOT("7020"),
    FUNC_STAT("7021"),
    REV_STAT("7022"),
    GIAI_ASSEMBLY("7023"),
    PROCESSOR_NUMBER("703*"),
    UIC_EXT("7040"),
    NHRN_PZN("710"),
    NHRN_CIP("711"),
    NHRN_CN("712"),
    NHRN_DRN("713"),
    NHRN_AIM("714"),
    CERT_NUMBER("723*"),
    PROTOCOL("7240"),
    DIMENSIONS("8001"),
    CMT_NUMBER("8002"),
    GRAI("8003"),
    GIAI("8004"),
    PRICE_PER_UNIT("8005"),
    ITIP("8006"),
    IBAN("8007"),
    PROD_TIME("8008"),
    OPTSEN("8009"),
    CPID("8010"),
    CPID_SERIAL("8011"),
    VERSION("8012"),
    GMN("8013"),
    GSRN_PROVIDER("8017"),
    GSRN_RECIPIENT("8018"),
    SRIN("8019"),
    REF_NUMBER("8020"),
    ITIP_CONTENT("8026"),
    COUPON_USA("8110"),
    POINTS("8111"),
    POSITIVE_OFFER_COUPON_USA("8121"),
    PRODUCT_URL("8200"),
    AGREEMENT_INTERNAL("90"),
    COMPANY_INTERNAL_1("91"),
    COMPANY_INTERNAL_2("92"),
    COMPANY_INTERNAL_3("93"),
    COMPANY_INTERNAL_4("94"),
    COMPANY_INTERNAL_5("95"),
    COMPANY_INTERNAL_6("96"),
    COMPANY_INTERNAL_7("97"),
    COMPANY_INTERNAL_8("98"),
    COMPANY_INTERNAL_9("99");

    private final String element;

    GS1Elements(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public static GS1Elements fromKey(String key) {
        switch (key) {
            case "gtin":
                return GTIN;
            case "lot number":
            case "batch number":
                return LOT_NUMBER;
            case "production date":
                return PROD_DATE;
            case "best before date":
                return BEST_BEFORE_DATE;
            case "expiration date":
                return EXP_DATE;
            case "serial number":
                return SERIAL_NUMBER;
            default:
                throw new IllegalArgumentException("invalid or not supported gs1 key");
        }
    }

    public static String getApplicationIdentifier(String gs1Group) {
        for (GS1Elements gs1Elements : GS1Elements.values()) {
            String ai = gs1Elements.getElement();
            if (ai.endsWith("*")) {
                ai = ai.substring(0, ai.length() - 1);
            }
            if (gs1Group.startsWith(ai) && ai.endsWith("*")) {
                return gs1Group.substring(0, ai.length() + 1);
            } else if (gs1Group.startsWith(ai)) {
                return ai;
            }
        }
        throw new IllegalArgumentException("Could not retrieve ai from value");
    }
}
