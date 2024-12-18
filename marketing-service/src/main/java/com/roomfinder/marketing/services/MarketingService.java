package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FilterRequest;
import com.roomfinder.marketing.controllers.dto.request.RoomSalePostRequest;
import com.roomfinder.marketing.controllers.dto.request.SearchPostRequest;
import com.roomfinder.marketing.controllers.dto.response.InfoMarketing;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;


public interface MarketingService {
    RoomSalePostResponse createPost(RoomSalePostRequest roomSalePostRequest);

    RoomSalePostResponse updatePost(String id, RoomSalePostRequest roomSalePostRequest);

    void deletePost(String id);

    RoomSalePostResponse getPostById(String id);

    PageResponse<RoomSalePostResponse> getPosts(int page, int size);

    PageResponse<RoomSalePostResponse> getPostsFeatured(int page, int size);

    PageResponse<RoomSalePostResponse> getPostsPromotional(int page, int size);

    RoomSalePostResponse getPostByPromotional(String id);

    PageResponse<RoomSalePostResponse> getPostFilter(FilterRequest filterRequest, int page, int size);

    PageResponse<RoomSalePostResponse> searchTerm(String searchRequest, int page, int size);

    PageResponse<RoomSalePostResponse> getPostByDistrict(int district, int page, int size);
    PageResponse<RoomSalePostResponse> searchPosts(SearchPostRequest searchRequest, int page, int size);

    PageResponse<RoomSalePostResponse> flitterPostWithStatusForUser(String status,int page,int size);

    PageResponse<RoomSalePostResponse> filterPostWithType(int type,int page, int size);
    InfoMarketing getInfoMarketing();
}