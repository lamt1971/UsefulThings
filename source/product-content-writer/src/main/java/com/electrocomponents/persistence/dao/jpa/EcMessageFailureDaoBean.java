package com.electrocomponents.persistence.dao.jpa;

import com.electrocomponents.model.data.linelevel.EcMessageFailureEntity;
import com.electrocomponents.model.domain.linelevel.EcMessageFailure;
import com.electrocomponents.persistence.dao.EcMessageFailureDao;
import com.electrocomponents.persistence.daomk2.GenericDaoMk2Jpa;
import com.electrocomponents.productcontentwriter.JndiConstants;

/*
 * ************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Ganesh Raut
 * Created : 10 Oct 2007 at 12:01:36
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
 * TODO add class-level javadoc.
 * @author Ganesh Raut
 */
public class EcMessageFailureDaoBean extends GenericDaoMk2Jpa<EcMessageFailure, EcMessageFailureEntity, Long> implements
        EcMessageFailureDao {

    /** Default constructor. */
    public EcMessageFailureDaoBean() {
        super(JndiConstants.ENTITY_MANAGER_JNDI_NAME_EMEA);
    }

    /**
     * @param jndi the jndi name
     */
    public EcMessageFailureDaoBean(final String jndi) {
        super(jndi);
    }
}
