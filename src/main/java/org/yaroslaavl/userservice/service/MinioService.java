package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.request.ImageUploadDto;

import java.util.UUID;

public interface MinioService {

    String upload(ImageUploadDto imageUploadDto, UUID companyId);

    String getObject(String objectName);

    boolean removeObject(String objectName);

}
