/**
 *
 */
package com.electrocomponents.model.domain.order;

import java.io.Serializable;

import com.electrocomponents.model.domain.DateTime;

/*
 * ************************************************************************************************ Copyright (c)
 * Electrocomponents Plc. Author : Ganesh Raut Created : 8 Aug 2007 at 11:40:18
 * ************************************************************************************************ Change History
 * ************************************************************************************************ Ref * Who * Date *
 * Description ************************************************************************************************ * * *
 * ************************************************************************************************
 */

/**
 * A class which encapsulates all the purchasing manager related info for an order.
 * @author Ganesh Raut
 */
public class PurchasingManagerOrderInfo implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 6516434024016443934L;

    /** The status of the order. */
    private String pmStatus;

    /** The time when the staus was changed. */
    private DateTime pmStatusDate;

    /** The Approver (Purchasing Manager). */
    private Long pmApprover;

    /** The Approver who approved the order. */
    private Long pmApprovedBy;

    /** The reason for approval. */
    private String pmReasonForApproval;

    /** The Purchasing manager comment. */
    private String pmComment;

    /** The Purchasing manager reply. */
    private String pmReply;

    /** The PM organisation Id. */
    private String pmOrganisationId;

    /** The PM department Id. */
    private Long pmDepartmentId;

    /** The PM amend state. */
    private String pmAmendState;

    /**
     * @return the pmAmendState
     */
    public String getPmAmendState() {
        return pmAmendState;
    }

    /**
     * @param pmAmendState the pmAmendState to set
     */
    public void setPmAmendState(final String pmAmendState) {
        this.pmAmendState = pmAmendState;
    }

    /**
     * @return the pmApprovedBy
     */
    public Long getPmApprovedBy() {
        return pmApprovedBy;
    }

    /**
     * @param pmApprovedBy the pmApprovedBy to set
     */
    public void setPmApprovedBy(final Long pmApprovedBy) {
        this.pmApprovedBy = pmApprovedBy;
    }

    /**
     * @return the pmApprover
     */
    public Long getPmApprover() {
        return pmApprover;
    }

    /**
     * @param pmApprover the pmApprover to set
     */
    public void setPmApprover(final Long pmApprover) {
        this.pmApprover = pmApprover;
    }

    /**
     * @return the pmComment
     */
    public String getPmComment() {
        return pmComment;
    }

    /**
     * @param pmComment the pmComment to set
     */
    public void setPmComment(final String pmComment) {
        this.pmComment = pmComment;
    }

    /**
     * @return the pmDepartmentId
     */
    public Long getPmDepartmentId() {
        return pmDepartmentId;
    }

    /**
     * @param pmDepartmentId the pmDepartmentId to set
     */
    public void setPmDepartmentId(final Long pmDepartmentId) {
        this.pmDepartmentId = pmDepartmentId;
    }

    /**
     * @return the pmOrganisationId
     */
    public String getPmOrganisationId() {
        return pmOrganisationId;
    }

    /**
     * @param pmOrganisationId the pmOrganisationId to set
     */
    public void setPmOrganisationId(final String pmOrganisationId) {
        this.pmOrganisationId = pmOrganisationId;
    }

    /**
     * @return the pmReasonForApproval
     */
    public String getPmReasonForApproval() {
        return pmReasonForApproval;
    }

    /**
     * @param pmReasonForApproval the pmReasonForApproval to set
     */
    public void setPmReasonForApproval(final String pmReasonForApproval) {
        this.pmReasonForApproval = pmReasonForApproval;
    }

    /**
     * @return the pmReply
     */
    public String getPmReply() {
        return pmReply;
    }

    /**
     * @param pmReply the pmReply to set
     */
    public void setPmReply(final String pmReply) {
        this.pmReply = pmReply;
    }

    /**
     * @return the pmStatus
     */
    public String getPmStatus() {
        return pmStatus;
    }

    /**
     * @param pmStatus the pmStatus to set
     */
    public void setPmStatus(final String pmStatus) {
        this.pmStatus = pmStatus;
    }

    /**
     * @return the pmStatusDate
     */
    public DateTime getPmStatusDate() {
        return pmStatusDate;
    }

    /**
     * @param pmStatusDate the pmStatusDate to set
     */
    public void setPmStatusDate(final DateTime pmStatusDate) {
        this.pmStatusDate = pmStatusDate;
    }
}
