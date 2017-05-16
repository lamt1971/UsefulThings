package com.electrocomponents.model.domain;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ed Ward
 * Created : 20 May 2007 at 11:00:47
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 */

/**
 * An enumeration of ISO 4217 Currency Codes.
 * @author Ed Ward
 */
public enum Currency {

    // Enumerate all ISO 4217 Currency Codes with some PFE action ;o)

    /*
     * TODO SS - Java does'nt recognise all Currency codes (this is a problem with core Java). We need to un-rem this before go live and
     * work out which codes work and which dont (these codes were downloaded from a standard list on xe.com
     */

    /** United Arab Emirates, Dirhams. */
    AED(java.util.Currency.getInstance("AED")),
    /** Afghanistan, Afghanis. */
    AFN(java.util.Currency.getInstance("AFN")),
    /** Albania, Leke. */
    ALL(java.util.Currency.getInstance("ALL")),
    /** Armenia, Drams. */
    AMD(java.util.Currency.getInstance("AMD")),
    /** Netherlands Antilles, Guilders (also called Florins). */
    ANG(java.util.Currency.getInstance("ANG")),
    /** Angola, Kwanza. */
    AOA(java.util.Currency.getInstance("AOA")),
    /** Argentina, Pesos. */
    ARS(java.util.Currency.getInstance("ARS")),
    /** Australia, Dollars. */
    AUD(java.util.Currency.getInstance("AUD")),
    /** Aruba, Guilders (also called Florins). */
    AWG(java.util.Currency.getInstance("AWG")),
    /** Azerbaijan, Manats [obsolete]. */
    AZM(java.util.Currency.getInstance("AZM")),
    /** Bosnia and Herzegovina, Convertible Marka. */
    BAM(java.util.Currency.getInstance("BAM")),
    /** Barbados, Dollars. */
    BBD(java.util.Currency.getInstance("BBD")),
    /** Bangladesh, Taka. */
    BDT(java.util.Currency.getInstance("BDT")),
    /** Bulgaria, Leva. */
    BGN(java.util.Currency.getInstance("BGN")),
    /** Bahrain, Dinars. */
    BHD(java.util.Currency.getInstance("BHD")),
    /** Burundi, Francs. */
    BIF(java.util.Currency.getInstance("BIF")),
    /** Bermuda, Dollars. */
    BMD(java.util.Currency.getInstance("BMD")),
    /** Brunei Darussalam, Dollars. */
    BND(java.util.Currency.getInstance("BND")),
    /** Bolivia, Bolivianos. */
    BOB(java.util.Currency.getInstance("BOB")),
    /** Brazil, Brazil Real. */
    BRL(java.util.Currency.getInstance("BRL")),
    /** Bahamas, Dollars. */
    BSD(java.util.Currency.getInstance("BSD")),
    /** Bhutan, Ngultrum. */
    BTN(java.util.Currency.getInstance("BTN")),
    /** Botswana, Pulas. */
    BWP(java.util.Currency.getInstance("BWP")),
    /** Belarus, Rubles. */
    BYR(java.util.Currency.getInstance("BYR")),
    /** Belize, Dollars. */
    BZD(java.util.Currency.getInstance("BZD")),
    /** Canada, Dollars. */
    CAD(java.util.Currency.getInstance("CAD")),
    /** Congo/Kinshasa, Congolese Francs. */
    CDF(java.util.Currency.getInstance("CDF")),
    /** Switzerland, Francs. */
    CHF(java.util.Currency.getInstance("CHF")),
    /** Chile, Pesos. */
    CLP(java.util.Currency.getInstance("CLP")),
    /** China, Yuan Renminbi. */
    CNY(java.util.Currency.getInstance("CNY")),
    /** China, Renminbi. */
    RMB(java.util.Currency.getInstance("CNY")),
    /** Colombia, Pesos. */
    COP(java.util.Currency.getInstance("COP")),
    /** Costa Rica, Colones. */
    CRC(java.util.Currency.getInstance("CRC")),
    /** Cuba, Pesos. */
    CUP(java.util.Currency.getInstance("CUP")),
    /** Cape Verde, Escudos. */
    CVE(java.util.Currency.getInstance("CVE")),
    /** Cyprus, Pounds. */
    CYP(java.util.Currency.getInstance("CYP")),
    /** Czech Republic, Koruny. */
    CZK(java.util.Currency.getInstance("CZK")),
    /** Djibouti, Francs. */
    DJF(java.util.Currency.getInstance("DJF")),
    /** Denmark, Kroner. */
    DKK(java.util.Currency.getInstance("DKK")),
    /** Dominican Republic, Pesos. */
    DOP(java.util.Currency.getInstance("DOP")),
    /** Algeria, Algeria Dinars. */
    DZD(java.util.Currency.getInstance("DZD")),
    /** Estonia, Krooni. */
    EEK(java.util.Currency.getInstance("EEK")),
    /** Egypt, Pounds. */
    EGP(java.util.Currency.getInstance("EGP")),
    /** Eritrea, Nakfa. */
    ERN(java.util.Currency.getInstance("ERN")),
    /** Ethiopia, Birr. */
    ETB(java.util.Currency.getInstance("ETB")),
    /** Euro Member Countries, Euro. */
    EUR(java.util.Currency.getInstance("EUR")),
    /** Fiji, Dollars. */
    FJD(java.util.Currency.getInstance("FJD")),
    /** Falkland Islands (Malvinas), Pounds. */
    FKP(java.util.Currency.getInstance("FKP")),
    /** United Kingdom, Pounds. */
    GBP(java.util.Currency.getInstance("GBP")),
    /** Georgia, Lari. */
    GEL(java.util.Currency.getInstance("GEL")),
    /** Ghana, Cedis. */
    GHC(java.util.Currency.getInstance("GHC")),
    /** Gibraltar, Pounds. */
    GIP(java.util.Currency.getInstance("GIP")),
    /** Gambia, Dalasi. */
    GMD(java.util.Currency.getInstance("GMD")),
    /** Guinea, Francs. */
    GNF(java.util.Currency.getInstance("GNF")),
    /** Guatemala, Quetzales. */
    GTQ(java.util.Currency.getInstance("GTQ")),
    /** Guyana, Dollars. */
    GYD(java.util.Currency.getInstance("GYD")),
    /** Hong Kong, Dollars. */
    HKD(java.util.Currency.getInstance("HKD")),
    /** Honduras, Lempiras. */
    HNL(java.util.Currency.getInstance("HNL")),
    /** Croatia, Kuna. */
    HRK(java.util.Currency.getInstance("HRK")),
    /** Haiti, Gourdes. */
    HTG(java.util.Currency.getInstance("HTG")),
    /** Hungary, Forint. */
    HUF(java.util.Currency.getInstance("HUF")),
    /** Indonesia, Rupiahs. */
    IDR(java.util.Currency.getInstance("IDR")),
    /** Israel, New Shekels. */
    ILS(java.util.Currency.getInstance("ILS")),
    /** India, Rupees. */
    INR(java.util.Currency.getInstance("INR")),
    /** Iraq, Dinars. */
    IQD(java.util.Currency.getInstance("IQD")),
    /** Iran, Rials. */
    IRR(java.util.Currency.getInstance("IRR")),
    /** Iceland, Kronur. */
    ISK(java.util.Currency.getInstance("ISK")),
    /** Jamaica, Dollars. */
    JMD(java.util.Currency.getInstance("JMD")),
    /** Jordan, Dinars. */
    JOD(java.util.Currency.getInstance("JOD")),
    /** Japan, Yen. */
    JPY(java.util.Currency.getInstance("JPY")),
    /** Kenya, Shillings. */
    KES(java.util.Currency.getInstance("KES")),
    /** Kyrgyzstan, Soms. */
    KGS(java.util.Currency.getInstance("KGS")),
    /** Cambodia, Riels. */
    KHR(java.util.Currency.getInstance("KHR")),
    /** Comoros, Francs. */
    KMF(java.util.Currency.getInstance("KMF")),
    /** Korea (North), Won. */
    KPW(java.util.Currency.getInstance("KPW")),
    /** Korea (South), Won. */
    KRW(java.util.Currency.getInstance("KRW")),
    /** Kuwait, Dinars. */
    KWD(java.util.Currency.getInstance("KWD")),
    /** Cayman Islands, Dollars. */
    KYD(java.util.Currency.getInstance("KYD")),
    /** Kazakhstan, Tenge. */
    KZT(java.util.Currency.getInstance("KZT")),
    /** Laos, Kips. */
    LAK(java.util.Currency.getInstance("LAK")),
    /** Lebanon, Pounds. */
    LBP(java.util.Currency.getInstance("LBP")),
    /** Sri Lanka, Rupees. */
    LKR(java.util.Currency.getInstance("LKR")),
    /** Liberia, Dollars. */
    LRD(java.util.Currency.getInstance("LRD")),
    /** Lesotho, Maloti. */
    LSL(java.util.Currency.getInstance("LSL")),
    /** Lithuania, Litai. */
    LTL(java.util.Currency.getInstance("LTL")),
    /** Latvia, Lati. */
    LVL(java.util.Currency.getInstance("LVL")),
    /** Libya, Dinars. */
    LYD(java.util.Currency.getInstance("LYD")),
    /** Morocco, Dirhams. */
    MAD(java.util.Currency.getInstance("MAD")),
    /** Moldova, Lei. */
    MDL(java.util.Currency.getInstance("MDL")),
    /** Madagascar, Ariary. */
    MGA(java.util.Currency.getInstance("MGA")),
    /** Macedonia, Denars. */
    MKD(java.util.Currency.getInstance("MKD")),
    /** Myanmar (Burma), Kyats. */
    MMK(java.util.Currency.getInstance("MMK")),
    /** Mongolia, Tugriks. */
    MNT(java.util.Currency.getInstance("MNT")),
    /** Macau, Patacas. */
    MOP(java.util.Currency.getInstance("MOP")),
    /** Mauritania, Ouguiyas. */
    MRO(java.util.Currency.getInstance("MRO")),
    /** Malta, Liri. */
    MTL(java.util.Currency.getInstance("MTL")),
    /** Mauritius, Rupees. */
    MUR(java.util.Currency.getInstance("MUR")),
    /** Maldives (Maldive Islands), Rufiyaa. */
    MVR(java.util.Currency.getInstance("MVR")),
    /** Malawi, Kwachas. */
    MWK(java.util.Currency.getInstance("MWK")),
    /** Mexico, Pesos. */
    MXN(java.util.Currency.getInstance("MXN")),
    /** Malaysia, Ringgits. */
    MYR(java.util.Currency.getInstance("MYR")),
    /** Mozambique, Meticais [obsolete]. */
    MZM(java.util.Currency.getInstance("MZM")),
    /** Namibia, Dollars. */
    NAD(java.util.Currency.getInstance("NAD")),
    /** Nigeria, Nairas. */
    NGN(java.util.Currency.getInstance("NGN")),
    /** Nicaragua, Cordobas. */
    NIO(java.util.Currency.getInstance("NIO")),
    /** Norway, Krone. */
    NOK(java.util.Currency.getInstance("NOK")),
    /** Nepal, Nepal Rupees. */
    NPR(java.util.Currency.getInstance("NPR")),
    /** New Zealand, Dollars. */
    NZD(java.util.Currency.getInstance("NZD")),
    /** Oman, Rials. */
    OMR(java.util.Currency.getInstance("OMR")),
    /** Panama, Balboa. */
    PAB(java.util.Currency.getInstance("PAB")),
    /** Peru, Nuevos Soles. */
    PEN(java.util.Currency.getInstance("PEN")),
    /** Papua New Guinea, Kina. */
    PGK(java.util.Currency.getInstance("PGK")),
    /** Philippines, Pesos. */
    PHP(java.util.Currency.getInstance("PHP")),
    /** Pakistan, Rupees. */
    PKR(java.util.Currency.getInstance("PKR")),
    /** Poland, Zlotych. */
    PLN(java.util.Currency.getInstance("PLN")),
    /** Paraguay, Guarani. */
    PYG(java.util.Currency.getInstance("PYG")),
    /** Qatar, Rials. */
    QAR(java.util.Currency.getInstance("QAR")),
    /** Romania, Lei [obsolete]. */
    ROL(java.util.Currency.getInstance("ROL")),
    /** Romania, New Lei. */
    RON(java.util.Currency.getInstance("RON")),
    /** Russia, Rubles. */
    RUB(java.util.Currency.getInstance("RUB")),
    /** Rwanda, Rwanda Francs. */
    RWF(java.util.Currency.getInstance("RWF")),
    /** Saudi Arabia, Riyals. */
    SAR(java.util.Currency.getInstance("SAR")),
    /** Solomon Islands, Dollars. */
    SBD(java.util.Currency.getInstance("SBD")),
    /** Seychelles, Rupees. */
    SCR(java.util.Currency.getInstance("SCR")),
    /** Sudan, Dinars [obsolete]. */
    SDD(java.util.Currency.getInstance("SDD")),
    /** Sweden, Kronor. */
    SEK(java.util.Currency.getInstance("SEK")),
    /** Singapore, Dollars. */
    SGD(java.util.Currency.getInstance("SGD")),
    /** Saint Helena, Pounds. */
    SHP(java.util.Currency.getInstance("SHP")),
    /** Slovenia, Tolars [obsolete]. */
    SIT(java.util.Currency.getInstance("SIT")),
    /** Slovakia, Koruny. */
    SKK(java.util.Currency.getInstance("SKK")),
    /** Sierra Leone, Leones. */
    SLL(java.util.Currency.getInstance("SLL")),
    /** Somalia, Shillings. */
    SOS(java.util.Currency.getInstance("SOS")),
    /** Suriname, Dollars. */
    SRD(java.util.Currency.getInstance("SRD")),
    /** Sao Tome and Principe, Dobras. */
    STD(java.util.Currency.getInstance("STD")),
    /** El Salvador, Colones. */
    SVC(java.util.Currency.getInstance("SVC")),
    /** Syria, Pounds. */
    SYP(java.util.Currency.getInstance("SYP")),
    /** Swaziland, Emalangeni. */
    SZL(java.util.Currency.getInstance("SZL")),
    /** Thailand, Baht. */
    THB(java.util.Currency.getInstance("THB")),
    /** Tajikistan, Somoni. */
    TJS(java.util.Currency.getInstance("TJS")),
    /** Turkmenistan, Manats. */
    TMM(java.util.Currency.getInstance("TMM")),
    /** Tunisia, Dinars. */
    TND(java.util.Currency.getInstance("TND")),
    /** Tonga, Pa'anga. */
    TOP(java.util.Currency.getInstance("TOP")),
    /** Turkey, New Lira. */
    TRY(java.util.Currency.getInstance("TRY")),
    /** Trinidad and Tobago, Dollars. */
    TTD(java.util.Currency.getInstance("TTD")),
    /** Taiwan, New Dollars. */
    TWD(java.util.Currency.getInstance("TWD")),
    /** Taiwan, New Dollars. */
    NTD(java.util.Currency.getInstance("TWD")),
    /** Tanzania, Shillings. */
    TZS(java.util.Currency.getInstance("TZS")),
    /** Ukraine, Hryvnia. */
    UAH(java.util.Currency.getInstance("UAH")),
    /** Uganda, Shillings. */
    UGX(java.util.Currency.getInstance("UGX")),
    /** United States of America, Dollars. */
    USD(java.util.Currency.getInstance("USD")),
    /** Uruguay, Pesos. */
    UYU(java.util.Currency.getInstance("UYU")),
    /** Uzbekistan, Sums. */
    UZS(java.util.Currency.getInstance("UZS")),
    /** Venezuela, Bolivares. */
    VEB(java.util.Currency.getInstance("VEB")),
    /** Viet Nam, Dong. */
    VND(java.util.Currency.getInstance("VND")),
    /** Vanuatu, Vatu. */
    VUV(java.util.Currency.getInstance("VUV")),
    /** Samoa, Tala. */
    WST(java.util.Currency.getInstance("WST")),
    /** Communaute Financiere Africaine BEAC, Francs. */
    XAF(java.util.Currency.getInstance("XAF")),
    /** Silver, Ounces. */
    XAG(java.util.Currency.getInstance("XAG")),
    /** Gold, Ounces. */
    XAU(java.util.Currency.getInstance("XAU")),
    /** East Caribbean Dollars. */
    XCD(java.util.Currency.getInstance("XCD")),
    /** International Monetary Fund (IMF) Special Drawing Rights. */
    XDR(java.util.Currency.getInstance("XDR")),
    /** Communaute Financiere Africaine BCEAO, Francs. */
    XOF(java.util.Currency.getInstance("XOF")),
    /** Palladium Ounces. */
    XPD(java.util.Currency.getInstance("XPD")),
    /** Comptoirs Francais du Pacifique Francs. */
    XPF(java.util.Currency.getInstance("XPF")),
    /** Platinum, Ounces. */
    XPT(java.util.Currency.getInstance("XPT")),
    /** Yemen, Rials. */
    YER(java.util.Currency.getInstance("YER")),
    /** South Africa, Rand. */
    ZAR(java.util.Currency.getInstance("ZAR")),
    /** Zambia, Kwacha. */
    ZMK(java.util.Currency.getInstance("ZMK")),
    /** Zimbabwe, Zimbabwe Dollars. */
    ZWD(java.util.Currency.getInstance("ZWD"));

    /** The java.util.Currency instance associated with the enum. */
    private java.util.Currency utilCurrency;

    /**
     * @param utilCurrency {@link #utilCurrency}
     */
    private Currency(final java.util.Currency utilCurrency) {
        this.utilCurrency = utilCurrency;
    }

    /**
     * @return {@link #utilCurrency}
     */
    public java.util.Currency getCurrency() {
        return utilCurrency;
    }

    /**
     * TODO provide an implementation that makes an intelligent guess of the currency This method should only be used when there is no
     * currency explicitly associated with a monetary value. We may want to implement specific rules here.
     * @return the default currency for the user or entity property etc.
     */
    public static Currency getDefaultCurrency() {
        return Currency.GBP;
    }

}
