package com.roomfinder.marketing.services;

import com.roomfinder.marketing.repositories.entities.PostImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface MediaService {

    Set<PostImage> uploadImagesRoomSalePosts(String postId, List<MultipartFile> files);
    Set<PostImage> uploadImagesCarousel(String carouselId, List<MultipartFile> files);
    Set<PostImage> uploadImagesNews(String newId, List<MultipartFile> files);
    Set<PostImage> uploadImagesCategory(String categoryId, List<MultipartFile> files);
    Set<PostImage> uploadImagesIncentiveProgram(String incentiveId, List<MultipartFile> files);
    Set<PostImage> uploadImagesRealEstateExperience(String realEstateExperienceId, List<MultipartFile> files);
     Set<PostImage> uploadImagesHospitalRoom(String postId, List<MultipartFile> files);
}
