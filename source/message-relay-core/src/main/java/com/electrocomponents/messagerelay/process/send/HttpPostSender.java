
package com.electrocomponents.messagerelay.process.send;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.io.HttpResponseParser;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electrocomponents.commons.exception.ElectroException;
import com.electrocomponents.messagerelay.support.BusinessMessage;


/**
 * Copyright (c) Electrocomponents Plc.
 * Author         : e0180383
 * Creation Date  : 15-Jun-2006
 * Creation Time  : 16:26:08
 * IDE            : IntelliJ IDEA 5
 * *******************************************************************************************************************
 * Description :
 * Send a message as a HTTP FORM POST and recieve and relay the response back to the internal system if required.
 * *******************************************************************************************************************
 * Change History
 * *******************************************************************************************************************
 * * Change   * Author   * Date         * Description
 * *******************************************************************************************************************
 * * New      * e0180383 *  15-Jun-2006 * New class created
 * *******************************************************************************************************************
 * * 10.1ELS  * e0161085 *  22-Jun-2009 * Commented logging of response.
 * *******************************************************************************************************************
 * * GEN-1228 * e0161085 *  26-Aug-2010 * Tidied Code.
 * *******************************************************************************************************************
 *
 * @author e0180383
 */
public class HttpPostSender extends Sender {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(HttpPostSender.class);

   /**
    * Process the incoming BusinessMessage to be sent via HTTP/S.
    * @param outboundMessage The incoming BusinessMessage object to be sent over HTTP/S.
    * @return Message.
    * @throws ElectroException Exception.
    */
    public BusinessMessage processMessage(final BusinessMessage outboundMessage) throws ElectroException {
       if (log.isDebugEnabled()) { log.debug("process : Method Start"); }

       String target = outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TARGET_URL);

       if (target == null) {
         String error = "processMessage : Error, CONFIG_TARGET_URL not specifed in incoming BusinessMessage, please correct.";
         log.fatal(error);
         throw new ElectroException("HttpPostSender." + error);
       }

       HttpPost post    = null;
       BusinessMessage    responseMessage = new BusinessMessage();

       if (log.isDebugEnabled()) {
         log.debug("processMessage : Target URL = " + target);
         log.debug("processMessage : Number of FORM POST parameters to be sent = " + outboundMessage.getPostParameters().size());
         log.debug("processMessage : Number of HTTP Headers to be sent = " + outboundMessage.getMessageHeaders().size());
       }

       String response = null;

       int timeOut = Integer.parseInt(outboundMessage.getConfigParameter(BusinessMessage.CONFIG_TIMEOUT_MILLISECONDS));

       if (timeOut == 0) {
         String error = "processMessage : Error, CONFIG_TIMEOUT_MILLISECONDS not specifed in incoming BusinessMessage, please correct.";
         log.fatal(error);
         throw new ElectroException("HttpPostSender." + error);
       }

       try {
          HttpClient client = new DefaultHttpClient();
          client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
          client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
          post = new HttpPost(target);

          // Add the HTTP Headers to be sent to the outgoing message.
          HashMap  headers        = outboundMessage.getMessageHeaders();
          Iterator headerIterator = headers.keySet().iterator();

          while (headerIterator.hasNext()) {
             String headerName = (String) headerIterator.next();
             String headerValue = (String) headers.get(headerName);

             if (headerName != null) {
                // e.g. "Content-Type", "application/x-www-form-urlencoded"
                post.addHeader(headerName, headerValue);
                if (log.isDebugEnabled()) {
                  log.debug("processMessage : Adding HTTP header, NAME=" + headerName + ",VALUE=" + headerValue);
                }
             }
          }

          // Add the HTTP FORM POST Parameters to be sent to the outgoing message.
          HashMap  parameters    = outboundMessage.getPostParameters();
          Iterator paramIterator = parameters.keySet().iterator();

          while (paramIterator.hasNext()) {
             String paramName  = (String) paramIterator.next();
             String paramValue = (String) parameters.get(paramName);

             if (paramName != null) {
                post.getParams().setParameter(paramName, paramValue);
                if (log.isDebugEnabled()) {
                  log.debug("processMessage : Adding HTTP FORM Parameter, NAME=" + paramName + ",VALUE=" + paramValue);
                }
             }
          }
          // Send the message via HTTP POST...
          HttpResponse httpResponse = client.execute(post);

          // If we didnt get a HTTP 200 R/C fall over..
          if (httpResponse.getStatusLine().getStatusCode() != 200) {
              int    responseCode = httpResponse.getStatusLine().getStatusCode();
              String responseMess = httpResponse.getStatusLine().getReasonPhrase();
              String error = "processMessage : An error occurred executing the FORM POST " + "to " + target + ", the HTTP R/C was " + responseCode + " the error message was " + responseMess;
              log.fatal(error);
              throw new ElectroException("HttpPostSender." + error);
          } else {

              //if (log.isDebugEnabled()) log.debug("processMessage : Response from " + target + " was :-\n" + response);

              // Copy the response HTTP headers back from the HTTPClient post object to the response BusinessMessage to be sent back.
              Header[] responseHeaders = httpResponse.getAllHeaders();
              for (int i = 0; i < responseHeaders.length; i++) {
                 Header responseHeader = responseHeaders[i];
                 if (log.isDebugEnabled()) { log.debug("processMessage : Adding Response Headers to Response Message, NAME="
                                                     + responseHeader.getName() + ",VALUE=" + responseHeader.getValue()); }
                 responseMessage.addMessageHeader(responseHeader.getName(), responseHeader.getValue());
              }

              // Copy the response payload back from the HTTPClient post object to the return Message to be sent back to the calling app.
           
              // TODO: is it really UTF-8?
              response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
              responseMessage.addStringPayload(BusinessMessage.PAYLOAD_RESPONSE_MESSAGE, response);
          }

       } catch (IOException ioe) {
          String error = "processMessage : An IOException was thrown posting the HTTP FORM POST " + "to "
                                            + target + ", the exception type was " + ioe.getClass().getName() + ", the details were " + ioe.getMessage();
          log.fatal(error);
          throw new ElectroException("HttpPostSender." + error, ioe);
       }
       finally {
          post.releaseConnection();
       }
       if (log.isDebugEnabled()) { log.debug("processMessage : Method Finish"); }
       return responseMessage;
    }

}
