package com.electrocomponents.model.domain.discount;

/**
 * The ways in which an order can be placed.
 */
public enum MethodOfPlacement {
    /** */
    INTERNET(1),
    /** */
    SUPPLIER_LINK(2),
    /** */
    WEB_CD(3),
    /** */
    E_PROCUREMENT(4),
    /** */
    PURCHASING_MANAGER(5),
    /** STORE_MANAGER. */
    STORE_MANAGER(6),
    /** EMAIL. */
    EMAIL(7),
    /** FAX. */
    FAX(8),
    /** PHONE is used for Quotation manager. */
    PHONE(9),
    /** EDI. */
    EDI(10),
    /** LETTER. */
    LETTER(11),
    /** Over the counter. */
    OTC(12),
    /** Trade Counter. */
    TC(13),
    /** OTC. */
    FS(14),
    /** OTC. */
    MSR(15),
    /** OTC. */
    TX(16);

    /** The value representing the enum in the database. */
    private int value;

    /**
     * @param value {@link #value}
     */
    private MethodOfPlacement(final int value) {
        this.value = value;
    }

    /**
     * @return {@link #value}
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @return {@link #value}
     */
    public String getValueString() {
        return String.valueOf(value);
    }

    /**
     * @param value the value representing the enum in the database
     * @return the Visibility enum associated with the supplied database value
     */
    public static MethodOfPlacement valueOf(final int value) {
        final MethodOfPlacement mop;

        switch (value) {
        case 1:
            mop = INTERNET;
            break;
        case 2:
            mop = SUPPLIER_LINK;
            break;
        case 3:
            mop = WEB_CD;
            break;
        case 4:
            mop = E_PROCUREMENT;
            break;
        case 5:
            mop = PURCHASING_MANAGER;
            break;
        case 6:
            mop = STORE_MANAGER;
            break;
        case 7:
            mop = EMAIL;
            break;
        case 8:
            mop = FAX;
            break;
        case 9:
            mop = PHONE;
            break;
        case 10:
            mop = EDI;
            break;
        case 11:
            mop = LETTER;
            break;
        case 12:
            mop = OTC;
            break;
        case 13:
            mop = TC;
            break;
        case 14:
            mop = FS;
            break;
        case 15:
            mop = MSR;
            break;
        case 16:
            mop = TX;
            break;
        default:
            mop = PURCHASING_MANAGER;
            break;
        }

        return mop;
    }
}
