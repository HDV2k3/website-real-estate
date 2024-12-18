// FirebaseStorageClient.java
package com.roomfinder.marketing.services.impl;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.roomfinder.marketing.constants.BucketConstants;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.entities.PostImage;
import com.roomfinder.marketing.repositories.entities.RoomInfo;
import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FirebaseStorageClient {

    private final StorageClient storageClient;

    public FirebaseStorageClient(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public String uploadFileToBucket(String bucketName, String folderName, MultipartFile file) {
        try {
            String normalizedFolderName = folderName.endsWith("/") ? folderName.substring(0, folderName.length() - 1) : folderName;
            String generatedFileName = generateUniqueFileName(file.getOriginalFilename());
            String fullObjectName = normalizedFolderName + "/" + generatedFileName;
            Storage storage = storageClient.bucket().getStorage();
            BlobInfo blobInfo = createBlobInfo(bucketName, fullObjectName, file.getContentType());
            storage.create(blobInfo, file.getBytes());
            makeFilePublic(bucketName, fullObjectName);
            return createPublicUrl(bucketName, fullObjectName);
        } catch (IOException e) {
            log.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String createPublicUrl(String bucketName, String objectName) {
        String encodedObjectName = URLEncoder.encode(objectName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return String.format(BucketConstants.URL_FIREBASE_API.getValue(), bucketName, encodedObjectName);
    }

    private void makeFilePublic(String bucketName, String objectName) {
        Storage storage = storageClient.bucket().getStorage();
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob != null) {
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        } else {
            log.error("File not found: {}", objectName);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    private BlobInfo createBlobInfo(String bucketName, String objectName, String contentType) {
        return BlobInfo.newBuilder(BlobId.of(bucketName, objectName))
                .setContentType(contentType)
                .build();
    }

    public String generateSignedUrl(String bucketName, String filePath) {
        Storage storage = storageClient.bucket().getStorage();
        Blob blob = storage.get(BlobId.of(bucketName, filePath));

        if (blob == null || !blob.exists()) {
            log.error("Image not found for filePath: {}", filePath);
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }

        return blob.signUrl(1, TimeUnit.HOURS).toString();
    }

    public void updatePostImagesWithSignedUrls(RoomSalePostEntity roomSalePostEntity) {
        // Kiểm tra null safety
        if (roomSalePostEntity == null ||
                roomSalePostEntity.getRoomInfo() == null ||
                roomSalePostEntity.getRoomInfo().getPostImages() == null ||
                roomSalePostEntity.getRoomInfo().getPostImages().isEmpty()) {
            return; // Không có ảnh để cập nhật, return sớm
        }

        Set<PostImage> updatedPostImages = roomSalePostEntity.getRoomInfo().getPostImages().stream()
                .filter(postImage -> postImage != null && postImage.getUrlImagePost() != null) // Thêm filter để loại bỏ null
                .map(this::generateSignedUrlForPostImage)
                .collect(Collectors.toSet());

        RoomInfo roomInfo = roomSalePostEntity.getRoomInfo();
        roomInfo.setPostImages(updatedPostImages);
        roomSalePostEntity.setRoomInfo(roomInfo);
    }

    public PostImage generateSignedUrlForPostImage(PostImage postImage) {
        // Kiểm tra null safety
        if (postImage == null || postImage.getUrlImagePost() == null) {
            return postImage; // Trả về nguyên bản nếu không có URL
        }

        try {
            String signedUrl = generateSignedUrl(
                    BucketConstants.BUCKET_NAME.getValue(),
                    BucketConstants.MARKETING_FOLDER.getValue() + postImage.getUrlImagePost()
            );

            return PostImage.builder()
                    .name(postImage.getName())
                    .type(postImage.getType())
                    .urlImagePost(signedUrl)
                    .build();
        } catch (Exception e) {
            log.error("Error generating signed URL for image: {}", postImage.getUrlImagePost(), e);
            return postImage; // Trả về nguyên bản nếu có lỗi
        }
    }
}