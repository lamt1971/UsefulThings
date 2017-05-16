package com.electrocomponents.commons.components.general.util;

import java.util.List;


/**
 * <pre>
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : C0951918
 * Created : 21 Dec 2015 at 07:17:43
 *
 * ************************************************************************************************
 * Change History
 * ************************************************************************************************
 * Ref      * Who      * Date       * Description
 * ************************************************************************************************
 *          *          *            *
 * ************************************************************************************************
 * </pre>
 */

/**
 * TODO add class-level javadoc.
 * @author c0951918
 */
public class UrlParameterDTO {
	
	/** Search term. */
	private String searchTerm;
	
	/** Applied dimension IDs. */
	private List<String> appliedDimensionIds;
	
	/** Page off set */
	private Integer pageOffset;
	
	/** Sort by */
	private String sortBy;
	
	/** Sort order */
	private String sortOrder;
	
	/** View mode */
	private String viewMode;

	/** Sort option */
	private String sortOption;

	/** Last selected*/
	private String lastSelected;

	/** Search type. */
	private String searchType;

	/** Production pack */
	private String productionPack;

	/** Offer name*/
	private String offerName;

	/** New products */
	private String newProducts;

	/** Lead time */
	private String leadTime;

	/** Disabled refinements */
	private String disabledRefinements;

	/**
	 * @return the searchTerm
	 */
	public String getSearchTerm() {
		return searchTerm;
	}

	/**
	 * @param searchTerm the searchTerm to set
	 */
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	/**
	 * @return the appliedDimensionIds
	 */
	public List<String> getAppliedDimensionIds() {
		return appliedDimensionIds;
	}

	/**
	 * @param appliedDimensionIds the appliedDimensionIds to set
	 */
	public void setAppliedDimensionIds(List<String> appliedDimensionIds) {
		this.appliedDimensionIds = appliedDimensionIds;
	}

	/**
	 * @return the pageOffset
	 */
	public Integer getPageOffset() {
		return pageOffset;
	}

	/**
	 * @param pageOffset the pageOffset to set
	 */
	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	/**
	 * @return the sortBy
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * @param sortBy the sortBy to set
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the viewMode
	 */
	public String getViewMode() {
		return viewMode;
	}

	/**
	 * @param viewMode the viewMode to set
	 */
	public void setViewMode(String viewMode) {
		this.viewMode = viewMode;
	}

	/**
	 * @return the sortOption
	 */
	public String getSortOption() {
		return sortOption;
	}

	/**
	 * @param sortOption the sortOption to set
	 */
	public void setSortOption(String sortOption) {
		this.sortOption = sortOption;
	}

	/**
	 * @return the lastSelected
	 */
	public String getLastSelected() {
		return lastSelected;
	}

	/**
	 * @param lastSelected the lastSelected to set
	 */
	public void setLastSelected(String lastSelected) {
		this.lastSelected = lastSelected;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return the productionPack
	 */
	public String getProductionPack() {
		return productionPack;
	}

	/**
	 * @param productionPack the productionPack to set
	 */
	public void setProductionPack(String productionPack) {
		this.productionPack = productionPack;
	}

	/**
	 * @return the offerName
	 */
	public String getOfferName() {
		return offerName;
	}

	/**
	 * @param offerName the offerName to set
	 */
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	/**
	 * @return the newProducts
	 */
	public String getNewProducts() {
		return newProducts;
	}

	/**
	 * @param newProducts the newProducts to set
	 */
	public void setNewProducts(String newProducts) {
		this.newProducts = newProducts;
	}

	/**
	 * @return the leadTime
	 */
	public String getLeadTime() {
		return leadTime;
	}

	/**
	 * @param leadTime the leadTime to set
	 */
	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	/**
	 * @return the disabledRefinements
	 */
	public String getDisabledRefinements() {
		return disabledRefinements;
	}

	/**
	 * @param disabledRefinements the disabledRefinements to set
	 */
	public void setDisabledRefinements(String disabledRefinements) {
		this.disabledRefinements = disabledRefinements;
	}


}
