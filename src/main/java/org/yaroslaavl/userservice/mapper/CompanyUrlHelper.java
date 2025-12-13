package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CompanyUrlHelper {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.bucket-name}")
    private String bucket;

    @Named("ToFullUrl")
    public String toFullUrl(String objectName) {
        if (objectName == null || objectName.isEmpty()) return "";
        return minioUrl + "/" + bucket + "/" + objectName;
    }
}