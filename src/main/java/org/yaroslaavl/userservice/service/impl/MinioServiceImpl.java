package org.yaroslaavl.userservice.service.impl;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
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
import java.util.HashMap;
import java.util.Map;
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

    @Value("${bucket.folder}")
    private String folder;

    private final MinioClient minioClient;

    /**
     * Uploads logos and banners to a storage service, processes the image files, and returns their URLs.
     *
     * @param imageUploadDto an object containing the logo and banner files to upload
     * @param companyId the unique identifier of the company for which the images are being uploaded
     * @return a map containing the image types (LOGO, BANNER) as keys and their corresponding URLs as values;
     *         returns null if no images are provided
     * @throws FileStorageException if an error occurs during the image upload or storage process
     */
    @Override
    @SneakyThrows
    public Map<ImageType, String> upload(ImageUploadDto imageUploadDto, UUID companyId) {

        HashMap<ImageType, String> result = new HashMap<>();

        if ((imageUploadDto.getLogo() == null || imageUploadDto.getLogo().isEmpty())
                && (imageUploadDto.getBanner() == null || imageUploadDto.getBanner().isEmpty())) {
            return null;
        }

        ensureBucketExistsAndPublic();

        if (imageUploadDto.getLogo() != null && !imageUploadDto.getLogo().isEmpty()) {
            String logoObject = imageProcessing(ImageType.LOGO, imageUploadDto.getLogo(), companyId);
            result.put(ImageType.LOGO, logoObject);
        }

        if (imageUploadDto.getBanner() != null && !imageUploadDto.getBanner().isEmpty()) {
            String bannerObject = imageProcessing(ImageType.BANNER, imageUploadDto.getBanner(), companyId);
            result.put(ImageType.BANNER, bannerObject);
        }

        return result;
    }

    @Override
    public String getObject(ImageType type, UUID companyId) {
        String formattedFolder = MessageFormat.format(folder, type);
        String objectName = objectExist(formattedFolder, companyId);

        if (objectName == null) {
            return null;
        }

        return minioUrl + "/" + bucket + "/" + objectName;
    }

    private String imageProcessing(ImageType imageType, MultipartFile file, UUID companyId) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String formattedFolder = MessageFormat.format(folder, imageType);
        String extension = getExtension(file);

        String existing = objectExist(formattedFolder, companyId);
        if (existing != null) {
            removeObject(existing);
        }

        String objectName = formattedFolder + companyId + "." + extension;
        putObject(bucket, objectName, file);

        return objectName;
    }

    @SneakyThrows
    private void putObject(String bucket, String objectName, MultipartFile file) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    @SneakyThrows
    private void removeObject(String objectName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    @SneakyThrows
    private String objectExist(String formattedFolder, UUID companyId) {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .prefix(formattedFolder + companyId)
                        .build()
        );

        for (Result<Item> result : results) {
            return result.get().objectName();
        }
        return null;
    }

    private void ensureBucketExistsAndPublic() throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [{
                    "Effect": "Allow",
                    "Principal": "*",
                    "Action": "s3:GetObject",
                    "Resource": "arn:aws:s3:::%s/*"
                  }]
                }
                """.formatted(bucket);

        minioClient.setBucketPolicy(
                SetBucketPolicyArgs.builder()
                        .bucket(bucket)
                        .config(policy)
                        .build()
        );
    }

    private String getExtension(MultipartFile file) {
        return switch (Objects.requireNonNull(file.getContentType())) {
            case "image/png" -> "png";
            case "image/jpg", "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("Unsupported image type");
        };
    }
}