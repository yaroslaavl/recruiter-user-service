package org.yaroslaavl.userservice.validation;

import jakarta.activation.MimetypesFileTypeMap;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;

import java.util.Set;

public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {

    private ImageType type;
    private static final Set<String> MIME_TYPES = Set.of("image/png", "image/webp", "image/jpg", "image/jpeg");

    @Override
    public void initialize(Image constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
        if (image == null || image.isEmpty()) {
            return false;
        }

        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        String contentType = mimetypesFileTypeMap.getContentType(image.getName());

        if (!MIME_TYPES.contains(contentType)) {
            return false;
        }

        if (ImageType.LOGO.equals(type)) {
            return validateSize(image, ImageType.LOGO);
        } else if (ImageType.BANNER.equals(type)) {
            return validateSize(image, ImageType.BANNER);
        }

        return false;
    }

    private boolean validateSize(MultipartFile image, ImageType type) {
        if (image == null || image.isEmpty()) {
            return false;
        }

        long size = image.getSize();

        return switch (type) {
            case LOGO -> size <= 500 * 1024;
            case BANNER -> size <= 3 * 1024 * 1024;
        };
    }
}
