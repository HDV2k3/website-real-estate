package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.repositories.entities.PostImage;
import com.roomfinder.marketing.services.MediaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaFacade {

    MediaService mediaService;

    public Set<PostImage> uploadImagesRoomSalePosts(String postId, List<MultipartFile> images) {
        return mediaService.uploadImagesRoomSalePosts(postId, images);
    }
    public Set<PostImage> uploadImagesCarousel(String carouselId, List<MultipartFile> images) {
        return mediaService.uploadImagesCarousel(carouselId, images);
    }
    public Set<PostImage> uploadImagesNews(String newId, List<MultipartFile> images) {
        return mediaService.uploadImagesNews(newId, images);
    }
    public Set<PostImage> uploadImagesCategory(String categoryId, List<MultipartFile> images) {
        return mediaService.uploadImagesCategory(categoryId, images);
    }

    public Set<PostImage> uploadImagesIncentiveProgram(String incentiveId, List<MultipartFile> images) {
        return mediaService.uploadImagesIncentiveProgram(incentiveId, images);
    }
    public Set<PostImage> uploadImagesRealEstateExperience(String realEstateExperienceId, List<MultipartFile> images) {
        return mediaService.uploadImagesRealEstateExperience(realEstateExperienceId, images);
    }

    public Set<PostImage> uploadImagesHospitalRoom(String id,List<MultipartFile> images)
    {
        return mediaService.uploadImagesHospitalRoom(id,images);
    }
}
