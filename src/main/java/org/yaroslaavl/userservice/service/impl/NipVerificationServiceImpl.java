package org.yaroslaavl.userservice.service.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.soap.AddressingFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.repository.CompanyRepository;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.integrations.GusRootElement;
import org.yaroslaavl.userservice.mapper.CompanyMapper;
import org.yaroslaavl.userservice.service.NipVerificationService;
import org.yaroslaavl.userservice.gus.*;
import org.yaroslaavl.userservice.service.soap.SoapHandlerResolver;
import org.yaroslaavl.userservice.service.soap.SoapMessageHandler;

import java.io.StringReader;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NipVerificationServiceImpl implements NipVerificationService {

    @Value("${gus.client_key}")
    private String gusClientKey;

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private final EmailVerificationServiceImpl emailVerificationService;

    private static final String SERVICE_STATUS = "StatusUslugi";

    @Override
    public CompanyExecutedDto verification(String nip, String email) {
        emailVerificationService.checkEmailVerification(email);

        Optional<Company> companyByNip = companyRepository.findCompanyByNip(nip);
        log.info("Entered nip: {}", nip);

        if (companyByNip.isEmpty()) {
            try {
                UslugaBIRzewnPubl uslugaBIRzewnPubl = new UslugaBIRzewnPubl();
                uslugaBIRzewnPubl.setHandlerResolver(new SoapHandlerResolver());
                IUslugaBIRzewnPubl port = uslugaBIRzewnPubl.getE3(new AddressingFeature());
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
                log.info("Report: {}", report);

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

        return companyMapper.toDto(companyByNip.get());
    }
}
