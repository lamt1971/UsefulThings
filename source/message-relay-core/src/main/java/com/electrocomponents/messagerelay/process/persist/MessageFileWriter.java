package com.electrocomponents.messagerelay.process.persist;

import com.electrocomponents.commons.components.general.io.FileControl;
import com.electrocomponents.commons.components.general.util.DateTimeUtils;
import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.process.MessageProcessor;
import com.electrocomponents.messagerelay.support.BusinessMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 21-Jun-2006
 * Creation Time  : 14:55:13
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Saves a BusinessMessage object to a file on disk in a standard format (use to log POR / Acks etc)
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  21-Jun-2006 * New class created
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class MessageFileWriter extends MessageProcessor  {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(MessageFileWriter.class);

   /**
    * Process the incoming BusinessMessage.
    * @param message The incoming JMS Message object.
    * @return Response Message.
    * @throws ElectroException Exception.
    */
    public BusinessMessage processMessage(final BusinessMessage message) throws ElectroException {
       if (log.isDebugEnabled()) { log.debug("processMessage : Method Start"); }
       BusinessMessage responseMsg = new BusinessMessage();

       save(message);

       if (log.isDebugEnabled()) { log.debug("processMessage : Method Finish"); }
       return responseMsg;
    }

    /**
     *  Save a specified message to the disk.
     *  The file will be saved as CONFIG_PERSISTANCE_FILENAME + CONFIG_TRANSACTION_FORMAT + CONFIG_TRANSACTION_TYPE
     *  CONFIG_PERSISTANCE_FILENAME = The path / 1st part of the filename, e.g. /jboss/serverapps/log/eprocurement-europe/OAG_POA_RESPONSE
     *  CONFIG_GENERATOR_HOSTNAME   = Present in both the response message and the inbedded
     *                                 outbound message - hostname of generating servers.
     *  @param  message The incoming BusinessMessage to be persisted to a file.
     *  @throws ElectroException Exception.
     */
    public synchronized void save(final BusinessMessage message) throws ElectroException {
        if (log.isDebugEnabled()) { log.debug("save : Method Start"); }

        // Generate the files path / filename...
        StringBuffer fileName = new StringBuffer();

        fileName.append(message.getConfigParameter(BusinessMessage.CONFIG_PERSISTANCE_FILENAME));
        fileName.append("_");
        fileName.append(DateTimeUtils.getFilenameTimestamp());

        // Check for duplicate filename and handle.
        boolean found = false;
        int count     = 0;

        while (!found) {
            File file = new File(fileName.toString() + count);
            found = (!file.exists());
            count++;
        }

        // Construct the final filename
        fileName.append(count - 1);
        // Remove any space characters from the file name
        String finalFileName = fileName.toString().replace(' ', '_');

        // Create the file content to be persisted...

        StringBuffer content  =  new StringBuffer();

        content.append("ELECTROCOMPONENTS ECOMMERCE BUSINESS MESSAGING SYSTEM\n");
        content.append("--------------- Message Log File v1.0 ---------------\n\n");
        content.append("Message Written To Disk : " + DateTimeUtils.getIso8601TimeStamp() + "\n\n");

        if (message.getOutboundMessage() != null) {
          content.append("Message Initiator Host : "
           +  message.getOutboundMessage().getConfigParameter(BusinessMessage.CONFIG_GENERATOR_HOSTNAME) + "\n");
          content.append("Message Relay Host     : " +  message.getConfigParameter(BusinessMessage.CONFIG_GENERATOR_HOSTNAME) + "\n\n");

          content.append("1).OUTBOUND MESSAGE :-\n\n");
          content.append(message.getOutboundMessage().toString() + "\n\n\n\n");

          content.append("2).RESPONSE MESSAGE :-\n\n");
        } else {
            content.append("Message Initiator Host : " +  message.getConfigParameter(BusinessMessage.CONFIG_GENERATOR_HOSTNAME));
        }
        content.append(message.toString());

        // Save the message to a file on the disk
        try {
            java.io.FileWriter fw;
            fw = FileControl.openFile(finalFileName);
            FileControl.writeDetails(fw, content.toString());
            FileControl.closeFile(fw);
        } catch (IOException ioe) {
            String error = "save : An IOException occurred when saving the message to "
              + finalFileName + ", the error detail was ; " + ioe.getMessage();
            log.fatal(error);
            throw new ElectroException("MessageFileWriter." + error, ioe);
        }
        if (log.isDebugEnabled()) { log.debug("save : Method Finish"); }
    }
}

