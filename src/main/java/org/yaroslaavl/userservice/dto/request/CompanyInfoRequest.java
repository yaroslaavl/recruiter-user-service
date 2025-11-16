package org.yaroslaavl.userservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.yaroslaavl.userservice.validation.Website;
import org.yaroslaavl.userservice.validation.groups.EditAction;

@Getter
@Setter
public class CompanyInfoRequest {

    @Min(value = 1, message = "{company.request.info.size.employee}", groups = EditAction.class)
    private Integer employeeCount;

    @Size(max = 500, message = "{company.request.info.size.description}", groups = EditAction.class)
    private String description;

    @Website(groups = EditAction.class)
    private String websiteUrl;
}
