package org.yaroslaavl.userservice.service.soap;

import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.HandlerResolver;
import jakarta.xml.ws.handler.PortInfo;

import java.util.Collections;
import java.util.List;

public class SoapHandlerResolver implements HandlerResolver {

    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        return Collections.singletonList(new SoapMessageHandler());
    }
}
