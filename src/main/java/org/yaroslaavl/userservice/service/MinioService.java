package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;

import java.util.Map;
import java.util.UUID;

public interface MinioService {

    Map<ImageType, String> upload(ImageUploadDto imageUploadDto, UUID companyId);

    String getObject(ImageType type, UUID companyId);
}
