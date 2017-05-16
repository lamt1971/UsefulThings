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
@org.hibernate.annotations.TypeDef(name = "last_mod_time", typeClass = com.electrocomponents.persistence.hibernate.LastModTimeUserType.class)
})
package com.electrocomponents.persistence.hibernate.javaobjectcache;