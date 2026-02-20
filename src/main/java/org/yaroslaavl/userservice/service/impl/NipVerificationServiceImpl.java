package org.yaroslaavl.userservice.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.ws.soap.AddressingFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.repository.CompanyRepository;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.integrations.GusRootElement;
import org.yaroslaavl.userservice.gus.*;
import org.yaroslaavl.userservice.mapper.CompanyMapper;
import org.yaroslaavl.userservice.service.NipVerificationService;
import org.yaroslaavl.userservice.service.soap.SoapHandlerResolver;
import org.yaroslaavl.userservice.service.soap.SoapMessageHandler;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.StringReader;
import java.security.cert.X509Certificate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NipVerificationServiceImpl implements NipVerificationService {

    @Value("${gus.client_key}")
    private String gusClientKey;

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;

    private static final String SERVICE_STATUS = "StatusUslugi";

    @PostConstruct
    public void init() {
        log.info("NIP verification service initialized for dev mode (SSL trust disabled)");
    }

    @Override
    public CompanyExecutedDto verification(String nip, String email) {
        Optional<Company> companyByNip = companyRepository.findCompanyByNip(nip);
        log.info("Entered nip: {}", nip);

        if (companyByNip.isEmpty()) {
            try {
                UslugaBIRzewnPubl uslugaBIRzewnPubl = new UslugaBIRzewnPubl();
                uslugaBIRzewnPubl.setHandlerResolver(new SoapHandlerResolver());
                IUslugaBIRzewnPubl port = uslugaBIRzewnPubl.getE3(new AddressingFeature());

                disableSslVerificationForCxf(port);

                String result = port.getValue(SERVICE_STATUS);
                if (SoapMessageHandler.sessionCookie.isEmpty() || !"1".equals(result)) {
                    String sid = port.zaloguj(gusClientKey);
                    SoapMessageHandler.sessionCookie = sid;
                }

                ObjectFactory objectFactory = new ObjectFactory();
                JAXBElement<String> nipParam = objectFactory.createParametryWyszukiwaniaNip(nip);
                ParametryWyszukiwania parametryWyszukiwania = new ParametryWyszukiwania();
                parametryWyszukiwania.setNip(nipParam);

                String report = port.daneSzukajPodmioty(parametryWyszukiwania);
                if (report == null || report.trim().isEmpty()) {
                    throw new RuntimeException("GUS API returned an empty or null report");
                }

                JAXBContext jaxbContext = JAXBContext.newInstance(GusRootElement.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                GusRootElement root = (GusRootElement) unmarshaller.unmarshal(new StringReader(report));

                return root.getDane();
            } catch (Exception e) {
                log.error("Error during verification NIP: {}", e.getMessage(), e);
                throw new RuntimeException("Error during request to GUS API: " + e.getMessage(), e);
            }
        }

        return companyMapper.toExecutedDto(companyByNip.get());
    }

    private void disableSslVerificationForCxf(IUslugaBIRzewnPubl port) {
        try {
            Client client = ClientProxy.getClient(port);
            HTTPConduit conduit = (HTTPConduit) client.getConduit();

            TLSClientParameters tlsParams = new TLSClientParameters();
            tlsParams.setDisableCNCheck(true);
            tlsParams.setTrustManagers(new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                    }
            });
            conduit.setTlsClientParameters(tlsParams);
        } catch (Exception e) {
            log.warn("Failed to disable SSL verification for CXF: {}", e.getMessage(), e);
        }
    }
}