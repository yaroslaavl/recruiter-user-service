package org.yaroslaavl.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.validation.Image;
import org.yaroslaavl.userservice.validation.groups.CreateAction;

@Getter
@Setter
public class ImageUploadDto {

    @Image(type = ImageType.LOGO, groups = CreateAction.class)
    private MultipartFile logo;

    @Image(type = ImageType.BANNER, groups = CreateAction.class)
    private MultipartFile banner;
}
