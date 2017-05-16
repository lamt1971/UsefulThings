/**
 * com.electrocomponents.bridgeprocess.service.core.interfaces.InterfaceService : InterfaceService.java
 * 3 Dec 2008 09:27:39
 * c0950078
 **/
package com.electrocomponents.bridgeprocess.service.core.interfaces;

/**<pre>
 * ********************************************************************************************************
 * Copyright (c) Electrocomponents Plc.
 *
 * Author  : Yogesh Patil
 * Created : 3 Dec 2008 at 09:27:39
 *
 * ********************************************************************************************************
 *
 *
 * ********************************************************************************************************
 * @author Yogesh Patil
 * ********************************************************************************************************
 * * Change History
 * ********************************************************************************************************
 * * Ref      * Who                 * Date       * Description
 * ********************************************************************************************************
 * </pre>
 **/
/**
 * @param <T> The type parameter.
 */
public interface InterfaceService<T> {

    /**
     * This method processes the Bridge Message arriving at MDB. This generic method is called by MDB listening to Queue/Topic.
     * @param message The message body. Can be interpreted by implementation in any suitable way.
     */
    void processBridgeMessage(T message);
}
