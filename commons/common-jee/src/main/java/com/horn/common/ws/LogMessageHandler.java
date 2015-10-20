package com.horn.common.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * @author LesinSA
 */
public class LogMessageHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String MESSAGE_ID_PARAM = "com.horn.common.ws.messageId";
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final Logger logger;

    public LogMessageHandler() {
        logger = LoggerFactory.getLogger(LogMessageHandler.class);
    }

    public LogMessageHandler(Logger logger) {
        this.logger = logger;
    }

    private static byte[] getBytes(String s) {
        return s != null ? s.getBytes(DEFAULT_CHARSET) : new byte[0];
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            if ((Boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
                String messageId = UUID.randomUUID().toString();
                context.put(MESSAGE_ID_PARAM, messageId);
                logMessage(context, "Request ");
            } else {
                logResponse(context);
            }
            return true;
        } catch (RuntimeException e) {
            logger.error("An error in handler occurs.", e);
            throw e;
        }
    }

    protected void logResponse(SOAPMessageContext context) {
        logMessage(context, "Response");
    }

    protected void logMessage(SOAPMessageContext context, String messageTitle) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // строка заголовка
            out.write(getBytes("["));
            out.write(getBytes(messageTitle));
            out.write(getBytes(" - "));
            String endpointAddress = (String) context.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
            out.write(getBytes(endpointAddress));
            out.write(getBytes(" ]"));
            String messageId = (String) context.get(MESSAGE_ID_PARAM);
            if (messageId != null) {
                out.write(getBytes(" - "));
                out.write(getBytes(messageId));
            }
            out.write(getBytes("\n"));
            // HTTP-заголовки

            // тело запроса/ответа
            SOAPMessage message = context.getMessage();
            message.writeTo(out);
            logger.debug(new String(out.toByteArray(), "UTF-8"));
        } catch (IOException e) {
            logger.error("An error in handler occurs.", e);
            throw new IllegalStateException(e);
        } catch (SOAPException e) {
            logger.error("An error in handler occurs.", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        logResponse(context);
        return false;
    }

    @Override
    public void close(MessageContext context) {
        // nothing to do
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }
}