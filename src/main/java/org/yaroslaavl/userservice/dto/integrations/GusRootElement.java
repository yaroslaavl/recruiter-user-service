package org.yaroslaavl.userservice.dto.integrations;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class GusRootElement {

    @XmlElement(name = "dane")
    private CompanyExecutedDto dane;
}
