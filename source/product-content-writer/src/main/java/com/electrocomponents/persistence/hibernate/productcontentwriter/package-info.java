/**
 * The hibernate-specific mechanism used to map custom user types
 * to database column(s). Since JPA doesn't provide a way to
 * associated user-defined database values with enums (only ordinal
 * values are used in JPA) we must specify enum types here.<p>
 *
 * The types are referenced by the entity using the hibernate
 * 'org.hibernate.annotations.Type' annotation. eg.
 *
 *     @org.hibernate.annotations.Type(type = "customer_group")
 *
 */
@org.hibernate.annotations.TypeDefs({
@org.hibernate.annotations.TypeDef(name = "date_time", typeClass = com.electrocomponents.persistence.hibernate.DateTimeUserType.class),
@org.hibernate.annotations.TypeDef(name = "ec_currency", typeClass = com.electrocomponents.persistence.hibernate.EcCurrencyUserType.class),
@org.hibernate.annotations.TypeDef(name = "last_mod_time", typeClass = com.electrocomponents.persistence.hibernate.LastModTimeUserType.class),
@org.hibernate.annotations.TypeDef(name = "money_precision_four_decimal", typeClass = com.electrocomponents.persistence.hibernate.MoneyPrecision4DPUserType.class),
@org.hibernate.annotations.TypeDef(name = "pack_type", typeClass = com.electrocomponents.persistence.hibernate.PackTypeUserType.class),
@org.hibernate.annotations.TypeDef(name = "product_lifecycle_status", typeClass = com.electrocomponents.persistence.hibernate.ProductLifecycleStatusUserType.class),
@org.hibernate.annotations.TypeDef(name = "product_number", typeClass = com.electrocomponents.persistence.hibernate.ProductNumberUserType.class),
@org.hibernate.annotations.TypeDef(name = "product_stock_policy", typeClass = com.electrocomponents.persistence.hibernate.ProductStockPolicyUserType.class),
@org.hibernate.annotations.TypeDef(name = "single_column_quantity", typeClass = com.electrocomponents.persistence.hibernate.QuantityUserType.class),
@org.hibernate.annotations.TypeDef(name = "rohs_status", typeClass = com.electrocomponents.persistence.hibernate.RohsStatusUserType.class),
@org.hibernate.annotations.TypeDef(name = "single_column_money", typeClass = com.electrocomponents.persistence.hibernate.SingleColumnMoneyUserType.class),
@org.hibernate.annotations.TypeDef(name = "site", typeClass = com.electrocomponents.persistence.hibernate.SiteUserType.class),
@org.hibernate.annotations.TypeDef(name = "vat_code", typeClass = com.electrocomponents.persistence.hibernate.VatCodeUserType.class)
})
package com.electrocomponents.persistence.hibernate.productcontentwriter;

