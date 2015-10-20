package com.horn.common.ws;

import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * @author LesinSA
 */
public class LogErrorMessageHandler extends LogMessageHandler {

    @Override
    protected void logMessage(SOAPMessageContext context, String messageTitle) {
        try {
            if (context.getMessage().getSOAPBody().getFault() != null) {
                super.logMessage(context, messageTitle);
            }
        } catch (SOAPException e) {
            throw new IllegalStateException(e);
        }
    }
}