package org.yaroslaavl.userservice.service.soap;

import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import java.util.*;

@Slf4j
public class SoapMessageHandler implements SOAPHandler<SOAPMessageContext> {

    public static String sessionCookie = "";

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            String sid = sessionCookie;
            if (sid != null && !sid.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, List<String>> headers = (Map<String, List<String>>)
                        context.get(MessageContext.HTTP_REQUEST_HEADERS);

                context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
                headers.put("sid", List.of(sid));
                log.info("Added sid as HTTP header: {}", sid);
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext soapMessageContext) {
        log.warn("SOAP fault intercepted");
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {

    }
}
