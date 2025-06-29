package org.yaroslaavl.userservice.service.impl;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;
import org.yaroslaavl.userservice.exception.FileStorageException;
import org.yaroslaavl.userservice.service.MinioService;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    @Value("${minio.bucket-name}")
    private String bucket;

    @Value("${minio.url}")
    private String minioUrl;

    private final MinioClient minioClient;
    private static final String FOLDER = "company_image_{0}/";

    @Override
    @SneakyThrows
    public String upload(ImageUploadDto imageUploadDto, UUID companyId) {
        try {
            if ((imageUploadDto.getLogo() == null || imageUploadDto.getLogo().isEmpty())
                    && (imageUploadDto.getBanner() == null || imageUploadDto.getBanner().isEmpty())) {
                return null;
            }

            boolean isPresent = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!isPresent) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            if (imageUploadDto.getLogo() != null && !imageUploadDto.getLogo().isEmpty()) {
                return imageProcessing(ImageType.LOGO, imageUploadDto.getLogo(), companyId);
            }

            if (imageUploadDto.getBanner() != null && !imageUploadDto.getBanner().isEmpty()) {
                return imageProcessing(ImageType.BANNER, imageUploadDto.getBanner(), companyId);
            }

            return null;
        } catch (MinioException me) {
            log.warn("Error occurred: {}", String.valueOf(me));
            log.warn("HTTP trace: {}", me.httpTrace());
            throw new FileStorageException("Could not store file in MinIO", me);
        }
    }

    @Override
    public String getObject(String objectName) {
        return "";
    }

    @Override
    public boolean removeObject(String objectName) {
        return false;
    }

    private String imageProcessing(ImageType imageType, MultipartFile file, UUID companyId) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String formattedFolder = MessageFormat.format(FOLDER, imageType);
        String extension = getExtension(file);

        putObject(bucket, formattedFolder, companyId, file, extension);

        return minioUrl + "/" + bucket + "/" + formattedFolder + companyId + extension;
    }

    @SneakyThrows
    private void putObject(String bucket, String objectName, UUID companyId, MultipartFile file, String extension) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectName + companyId + extension)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
    }

    private String getExtension(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        return switch (Objects.requireNonNull(file.getContentType())) {
            case "image/png" -> "png";
            case "image/jpg", "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("Unsupported image type: " + file.getContentType());
        };
    }
}
