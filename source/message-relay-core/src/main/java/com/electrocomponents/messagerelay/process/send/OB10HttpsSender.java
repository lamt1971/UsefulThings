package com.electrocomponents.messagerelay.process.send;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.electrocomponents.messagerelay.support.BusinessMessage;

/**
 * Copyright (c) Electrocomponents Plc.
 * 
 * @author Ram Tripathi
 */
public class OB10HttpsSender extends Sender {

    /**
     * Log4J.
     */
    private static Log log = LogFactory.getLog(OB10HttpsSender.class);

    /**
     * OBE Number.
     */
    private String obeNumber;
    /**
     * OBE URL.
     */
    private String obeUrl;
    /**
     * OBE User.
     */
    private String obeUser;
    /**
     * OBE Password.
     */
    private String obePassword;

    /**
     * Locale.
     */
    private String locale;
    /**
     * Document Nbr.
     */
    private String documentNbr;
    /**
     * OBE File Name.
     */
    private String obeFileName;
    /**
     * Document Date.
     */
    private String documentDate;
    /**
     * Document Type.
     */
    private String documentType;
    /**
     * Customer Identifier.
     */
    private String customerIdentifier;
    /**
     * Account Number.
     */
    private String accountNumber;

    /**
     * Payload.
     */
    private String document;

    private String ob10TemporaryFilePath;
    /*
     * File Extension.
     */
    private String obeFileExtension;

    /**
     * Process the message which is picked up from the queue. Once the message
     * has been picked up from the queue, retrieve the configuration parameters,
     * header and payload, and then send the document to OBE. Generate a
     * suitable response to tell the einvoicing hub that everything is ok.
     * 
     * @param message
     *            Business Message.
     * @return response saying that the transmission was ok / not ok.
     */
    @Override
    public BusinessMessage processMessage(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Started");
        }
        BusinessMessage responseMsg;

        responseMsg = processInvoice(message);

        if (log.isDebugEnabled()) {
            log.debug("processMessage() : Finished");
        }

        return responseMsg;
    }

    /**
     * ProcessInvoice.
     * 
     * @param message
     *            Business Message.
     * @return BusinessMessage message. Do the actual processing of the invoice.
     *         This is a workaround so that the steps can be synchronized.
     * 
     *         * THIS IS SYNCHRONISED AS OBE CLIENT IS NOT THREAD SAFE :o(
     */
    public synchronized BusinessMessage processInvoice(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("processInvoice() : Started");
            log.debug("processMessage() : getting Configuration Parameters");
        }

        BusinessMessage responseMsg;
        retrieveConfigurationParameters(message);

        // retrieve the header details
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : getting Message Headers");
        }
        retrieveMessageHeaders(message);

        // get the payload
        if (log.isDebugEnabled()) {
            log.debug("processMessage() : getting the Payload");
        }
        retrievePayLoad(message);

        // Send the message and generate the response back to the einvoicing hub

        responseMsg = sendToOBEHttps();

        if (log.isDebugEnabled()) {
            log.debug("processInvoice() : Finished");
        }
        return responseMsg;
    }

    /**
     * retrieveConfigurationParameters Pull off the configuration parameters
     * from the message.
     * 
     * @param message
     *            Business Message.
     */
    private void retrieveConfigurationParameters(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("retrieveConfigurationParameters() : Started");
        }
        obeNumber = message.getConfigParameter("OB10HTTPSNumber");
        obeUser = message.getConfigParameter("OB10HTTPSUser");
        obePassword = message.getConfigParameter("OB10HTTPSPassword");
        obeUrl = message.getConfigParameter("OB10HTTPSUrl");
        obeFileName = message.getConfigParameter("OB10HTTPSFileName");
        ob10TemporaryFilePath = message.getConfigParameter("OB10_TEMPORARY_FILE_PATH");
        locale = message.getConfigParameter("Locale");
        documentNbr = message.getConfigParameter("DocumentNbr");
        documentDate = message.getConfigParameter("DocumentDate");
        documentType = message.getConfigParameter("DocumentType");
        customerIdentifier = message.getConfigParameter("CustomerIdentifier");
        accountNumber = message.getConfigParameter("AccountNumber");
        obeFileExtension = message.getConfigParameter("ObeFileExtension");

        if (log.isDebugEnabled()) {
            log.debug("retrieveConfigurationParameters() : OBENumber = " + obeNumber);
            log.debug("retrieveConfigurationParameters() : OBEUrl = " + obeUrl);
            log.debug("retrieveConfigurationParameters() : OBEUser " + obeUser);
            log.debug("retrieveConfigurationParameters() : OBEPassword = " + obePassword);
            log.debug("retrieveConfigurationParameters() : locale = " + locale);
            log.debug("retrieveConfigurationParameters() : documentNbr = " + documentNbr);
            log.debug("retrieveConfigurationParameters() : OBEFileName = " + obeFileName);
            log.debug("retrieveConfigurationParameters() : ob10TemporaryFilePath = " + ob10TemporaryFilePath);
            log.debug("retrieveConfigurationParameters() : documentDate = " + documentDate);
            log.debug("retrieveConfigurationParameters() : documentType = " + documentType);
            log.debug("retrieveConfigurationParameters() : customerIdentifier = " + customerIdentifier);
            log.debug("retrieveConfigurationParameters() : accountNumber = " + accountNumber);
            log.debug("retrieveConfigurationParameters() : obeFileExtension = " + obeFileExtension);
            log.debug("retrieveConfigurationParameters() : Finished");
        }
    }

    /**
     * retrieveMessageHeaders Pull off the message headers from the message.
     * Currently no header paramters are used.
     * 
     * @param message
     *            Business Message.
     */
    private void retrieveMessageHeaders(final BusinessMessage message) {

    }

    /**
     * retrievePayLoad Pull off the document text from the message.
     * 
     * @param message
     *            Business Message.
     */
    private void retrievePayLoad(final BusinessMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("retrievePayLoad() : Started");
        }
        document = message.getStringPayload("OB10HTTPSDocument");

        if (log.isDebugEnabled()) {
            log.debug("retrievePayLoad() : document = " + document);
            log.debug("retrievePayLoad() : Finished");
        }
    }

    /**
     * Send the document to Tungsten through HTTPS. The settings for the
     * connection to OBE is retrieved from the configuration parameters in the
     * message.
     * 
     * THIS IS SYNCHRONISED AS OBE CLIENT IS NOT THREAD SAFE :o(
     * 
     * @return response message saying if process is sucessful or not.
     */
    private BusinessMessage sendToOBEHttps() {
        if (log.isDebugEnabled()) {
            log.debug("sendToOBEHttps() : Started");
        }

        BusinessMessage responseMsg = null;
        if (log.isDebugEnabled()) {
            log.debug("sendToOBEHttps() : Got Version");
            log.debug("sendToOBEHttps() : 1- " + obeUrl);
            log.debug("sendToOBEHttps() : OBENumber " + obeNumber);
            log.debug("sendToOBEHttps() : OBEUser " + obeUser);
            log.debug("sendToOBEHttps() : OBEPassword " + obePassword);
        }

        try {

            if (obeUrl == null) {
                if (log.isDebugEnabled()) {
                    log.error("sendToOBEHttps() : OB10 Https url is  null   obeUrl-  " + obeUrl);
                }

                responseMsg = createResponse("OB10 Https url is null  obeUrl -  " + obeUrl);

                return responseMsg;
            }

            if (log.isDebugEnabled()) {
                log.debug("sendToOBEHttps() : Creating https client ");
            }

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(obeUrl);
            String userPassword = obeNumber + ":" + obePassword;
            String basicAuth = "Basic " + new String(Base64.encodeBase64(userPassword.getBytes()));
            httpPost.setHeader("Authorization", basicAuth);

            MultipartEntity mpEntity = new MultipartEntity();

            File invoiceFile = new File(ob10TemporaryFilePath + obeFileName + obeFileExtension);

            boolean fileCreated = invoiceFile.createNewFile();

            if (log.isDebugEnabled()) {
                log.debug("Temporary file name is  " + invoiceFile.getName() + ". File Created OK " + fileCreated);
            }

            FileUtils.writeStringToFile(invoiceFile, document);

            FileBody fileBody = new FileBody(invoiceFile, obeFileName + obeFileExtension, "application/xml", "UTF-8");
            mpEntity.addPart("attachment", fileBody);
            httpPost.setEntity(mpEntity);

            HttpResponse response = client.execute(httpPost);

            if (log.isDebugEnabled()) {
                log.debug("sendToOBEHttps() sent ");

            }
            if (invoiceFile.exists()) {
                log.debug("File " + invoiceFile.getPath() + " exists. Deleting");
                invoiceFile.delete();
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                if (log.isDebugEnabled()) {
                    log.info("sendToOBEHttps() : Response Status  -  " + response.getStatusLine());
                    if (response.getEntity() != null) {
                        log.debug("Response content length: " + response.getEntity().getContentLength());
                        log.debug(response.getEntity().getContent());
                    } else {
                        log.debug("response.getEntity() is null !");
                    }
                }
                responseMsg = createResponse("OK");
            }

            else if (response.getStatusLine().getStatusCode() == 401) {
                if (log.isDebugEnabled()) {
                    log.error("sendToOBEHttps() : Response Status  -  " + response.getStatusLine());
                }

                responseMsg = createResponse("Unauthorized  wrong Username Or password error    Response-  " + 401);

            } else if (response.getStatusLine().getStatusCode() == 404) {
                if (log.isDebugEnabled()) {
                    log.error("sendToOBEHttps() : Response Status  -  " + response.getStatusLine());
                }

                responseMsg = createResponse("Resource Not found    Response-  " + 404);

            }

        } catch (UnknownHostException unknownHost) {

            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : UnknownHostException  Error -  " + unknownHost.getMessage());
            }
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : UnknownHostException  Error -  " + unknownHost.getStackTrace());
            }
            responseMsg = createResponse("UnknownHostException  Error -  " + unknownHost.getMessage());

        }

        catch (MalformedURLException mue) {
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : Malformed Exception Error -  " + mue.getMessage());
            }
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : Malformed Exception Error -  " + mue.getStackTrace());
            }
            responseMsg = createResponse("Malformed Exception Error -  " + mue.getMessage());
        } catch (IOException ioe) {
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : IOException Error -  " + ioe.getMessage());
            }
            ioe.printStackTrace();
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : IOException Error -  " + ioe.getStackTrace());
            }
            responseMsg = createResponse("IOException Error -  " + ioe.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : Exception Error -  " + e.getMessage());
            }
            if (log.isDebugEnabled()) {
                log.error("sendToOBEHttps() : Exception Error -  " + e.getStackTrace());
            }
            responseMsg = createResponse("Exception Error -  " + e.getMessage());
        }

        if (log.isDebugEnabled()) {
            log.debug("sendToOBEHttps() : Finished");
        }

        return responseMsg;
    }

    /**
     * createResponse Create a response for sending back to the einvoicing hub.
     * 
     * @param responseMessage
     *            response Message.
     * @return response message Business Message.
     */
    private BusinessMessage createResponse(final String responseMessage) {
        if (log.isDebugEnabled()) {
            log.debug("createResponse() : Started");
            log.debug("Message is: " + responseMessage);
        }
        HashMap configParam = new HashMap();
        HashMap payLoad = new HashMap();
        BusinessMessage responseMsg = new BusinessMessage();

        // Need to add the following as they are used to update the status
        // on einv_header or the audit trail in einv_transfer_audit
        configParam.put("DocumentNbr", documentNbr);
        configParam.put("Locale", locale);
        configParam.put("DocumentDate", documentDate);
        configParam.put("DocumentType", documentType);
        configParam.put("CustomerIdentifier", customerIdentifier);
        configParam.put("AccountNumber", accountNumber);

        configParam.put("OBEFileName", obeFileName);

        responseMsg.setConfigParameters(configParam);

        // Send the actual message
        payLoad.put("ResponseMessage", responseMessage);
        responseMsg.setStringPayloads(payLoad);
        if (log.isDebugEnabled()) {
            log.debug("createResponse() : Setting payload to " + responseMessage);
        }
        if (log.isDebugEnabled()) {
            log.debug("createResponse() : Finished");
        }
        return responseMsg;
    }

}
