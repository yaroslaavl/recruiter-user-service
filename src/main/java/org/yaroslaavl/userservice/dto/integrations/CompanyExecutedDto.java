package org.yaroslaavl.userservice.dto.integrations;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class CompanyExecutedDto {

    @XmlElement(name = "Nazwa")
    private String name;

    @XmlElement(name = "Nip")
    private String nip;

    @XmlElement(name = "Wojewodztwo")
    private String voivodeship;

    @XmlElement(name = "Miejscowosc")
    private String city;

    @XmlElement(name = "KodPocztowy")
    private String postCode;

    @XmlElement(name = "Ulica")
    private String street;
}
