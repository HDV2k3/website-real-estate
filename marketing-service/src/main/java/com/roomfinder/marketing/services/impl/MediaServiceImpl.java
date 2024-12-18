package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.constants.BucketConstants;
import com.roomfinder.marketing.constants.Status;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.repositories.*;
import com.roomfinder.marketing.repositories.entities.PostImage;
import com.roomfinder.marketing.services.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.roomfinder.marketing.constants.Status.ACTIVE;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MediaServiceImpl implements MediaService {

    FirebaseStorageClient firebaseStorageClient;
    RoomSalePostRepository roomSalePostRepository;
    CarouselRepository carouselRepository;
    NewsRepository newsRepository;
    CategoryRepository categoryRepository;
    IncentiveProgramRepository incentiveProgramRepository;
    RealEstateExperienceRepository realEstateExperienceRepository;
    HospitalRoomRepository hospitalRoomRepository;
    @Override
    public Set<PostImage> uploadImagesRoomSalePosts(String postId, List<MultipartFile> files) {
        var roomSalePostEntity = roomSalePostRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            log.info("No files to upload for postId: {}", postId);
            return Set.of();
        }

        Set<PostImage> postImages = uploadImagesToFolder(files, BucketConstants.MARKETING_FOLDER.getValue());
        roomSalePostEntity.getRoomInfo().getPostImages().addAll(postImages);
        roomSalePostEntity.setStatus(ACTIVE.name());
           roomSalePostRepository.save(roomSalePostEntity);
        return postImages;
    }
    @Override
    public Set<PostImage> uploadImagesHospitalRoom(String postId, List<MultipartFile> files) {
        var hospitalRoomEntity = hospitalRoomRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            log.info("No files to upload for postId: {}", postId);
            return Set.of();
        }

        Set<PostImage> postImages = uploadImagesToFolder(files, BucketConstants.MARKETING_FOLDER.getValue());
        hospitalRoomEntity.getPostImages().addAll(postImages);
        hospitalRoomRepository.save(hospitalRoomEntity);
        return postImages;
    }

    @Override
    public Set<PostImage> uploadImagesCarousel(String carouselId, List<MultipartFile> files) {
        var carouselEntity = carouselRepository.findById(carouselId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            return Set.of();
        }

        Set<PostImage> carouselImages = uploadImagesToFolder(files, BucketConstants.CAROUSEL_FOLDER.getValue());
        carouselEntity.getPostImages().addAll(carouselImages);
        carouselRepository.save(carouselEntity);
        return carouselImages;
    }

    @Override
    public Set<PostImage> uploadImagesNews(String newId, List<MultipartFile> files) {
        var newsEntity = newsRepository.findById(newId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            return Set.of();
        }

        Set<PostImage> newsImages = uploadImagesToFolder(files, BucketConstants.MARKET_AND_TREND_FOLDER.getValue());
        newsEntity.getPostImages().addAll(newsImages);
        newsRepository.save(newsEntity);
        return newsImages;
    }

    @Override
    public Set<PostImage> uploadImagesCategory(String categoryId, List<MultipartFile> files) {
        var categoryEntity = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            return Set.of();
        }

        Set<PostImage> categoryImages = uploadImagesToFolder(files, BucketConstants.CATEGORY_FOLDER.getValue());
        categoryEntity.getPostImages().addAll(categoryImages);
        categoryRepository.save(categoryEntity);
        return categoryImages;
    }

    @Override
    public Set<PostImage> uploadImagesIncentiveProgram(String incentiveId, List<MultipartFile> files) {
        var incentiveProgramEntity = incentiveProgramRepository.findById(incentiveId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            log.info("No files to upload for incentiveId: {}", incentiveId);
            return Set.of();
        }

        Set<PostImage> incentiveImages = uploadImagesToFolder(files, BucketConstants.INCENTIVE_FOLDER.getValue());
        incentiveProgramEntity.getPostImages().addAll(incentiveImages);
        incentiveProgramEntity.setStatus(ACTIVE);
        incentiveProgramRepository.save(incentiveProgramEntity);
        return incentiveImages;
    }

    @Override
    public Set<PostImage> uploadImagesRealEstateExperience(String realEstateExperienceId, List<MultipartFile> files) {
        var realEstateExperienceEntity = realEstateExperienceRepository.findById(realEstateExperienceId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (files == null || files.isEmpty()) {
            log.info("No files to upload for realEstateExperienceId: {}", realEstateExperienceId);
            return Set.of();
        }

        Set<PostImage> experienceImages = uploadImagesToFolder(files, BucketConstants.REAL_ESTATE_EXPERIENCE_FOLDER.getValue());
        realEstateExperienceEntity.getPostImages().addAll(experienceImages);
        realEstateExperienceRepository.save(realEstateExperienceEntity);
        return experienceImages;
    }

    /**
     * Generic method to upload multiple files to a specified folder.
     */
    private Set<PostImage> uploadImagesToFolder(List<MultipartFile> files, String folder) {
        return files.stream()
                .map(file -> uploadSingleImage(file, folder))
                .collect(Collectors.toSet());
    }

    /**
     * Uploads a single file to a specified folder and returns a PostImage object.
     */
    private PostImage uploadSingleImage(MultipartFile file, String folder) {
        String storedFileName = firebaseStorageClient.uploadFileToBucket(
                BucketConstants.BUCKET_NAME.getValue(),
                folder,
                file
        );

        return PostImage.builder()
                .name(file.getOriginalFilename())
                .type(Optional.ofNullable(file.getContentType()).orElseThrow(() -> new AppException(ErrorCode.FILE_UPLOAD_FAILED)))
                .urlImagePost(storedFileName)
                .build();
    }

}
