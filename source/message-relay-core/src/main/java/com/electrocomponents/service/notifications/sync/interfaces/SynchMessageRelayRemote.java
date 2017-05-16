package com.electrocomponents.service.notifications.sync.interfaces;

import javax.ejb.Remote;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 16-Jun-2006
 * Creation Time  : 11:26:18
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * A EJB3 Stateless Session Bean that can be executed by remote systems to relay BusinessMessages to remote hosts.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  16-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
@Remote
public interface SynchMessageRelayRemote extends SynchMessageRelay {

}
