package com.electrocomponents.model.domain;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Stuart Sim
 * Created : 7 Nov 2007 at 17:29:51
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
 * Core attibutes of a Plant / Site.
 * @author Stuart Sim
 */
public interface BasePlant {

    /**
     * @return the ersCode
     */
    String getErsCode();

    /**
     * @param ersCode the ersCode to set
     */
    void setErsCode(final String ersCode);

    /**
     * @return the ersSalesOrg
     */
    String getErsSalesOrg();

    /**
     * @param ersSalesOrg the ersSalesOrg to set
     */
    void setErsSalesOrg(final String ersSalesOrg);

    /**
     * @return the warehouse
     */
    Boolean getWarehouse();

    /**
     * @param warehouse the warehouse to set
     */
    void setWarehouse(final Boolean warehouse);

    /**
     * @return the tradeCounter
     */
    Boolean getTradeCounter();

    /**
     * @param tradeCounter the tradeCounter to set
     */
    void setTradeCounter(final Boolean tradeCounter);
    
    /**
	 * @return the isStoreFront
	 */
	Boolean getIsStoreFront();

	/**
	 * @param isStoreFront the isStoreFront to set
	 */
	void setIsStoreFront(Boolean isStoreFront);
}
