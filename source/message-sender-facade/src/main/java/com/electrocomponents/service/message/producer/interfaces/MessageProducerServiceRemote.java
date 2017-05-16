package com.electrocomponents.service.message.producer.interfaces;

import javax.ejb.Remote;

/**
 * The message producer remote service
 * 
 * @author Malcolm Boyle
 */
@Remote
public interface MessageProducerServiceRemote extends MessageProducerService {
    /*
     * MessageProducerServiceLocal is currently defined as Marker interfaces. Method prototype should be added when required.
     */
}
