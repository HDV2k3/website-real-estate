package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.GenericResponseAI;
import com.roomfinder.marketing.controllers.dto.PageResponse;
import com.roomfinder.marketing.controllers.dto.request.FilterRequest;
import com.roomfinder.marketing.controllers.dto.request.RoomSalePostRequest;
import com.roomfinder.marketing.controllers.dto.request.SearchPostRequest;
import com.roomfinder.marketing.controllers.dto.response.InfoMarketing;
import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.services.MarketingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MarketingFacade {

    MarketingService marketingService;



    /**
     * Retrieves a room sale post by its ID.
     *
     * @param id the post ID
     * @return the RoomSalePostResponse
     */
    public RoomSalePostResponse getRoomSalePostById(String id) {
        return marketingService.getPostById(id);
    }
    /**
     * Retrieves a room sale post promotional by its ID.
     *
     * @param id the post ID
     * @return the RoomSalePostResponse
     */
    public RoomSalePostResponse getRoomSalePostPromotionalById(String id) {
        return marketingService.getPostByPromotional(id);
    }
    /**
     * Creates a new room sale post.
     *
     * @param request the request data
     * @return the created RoomSalePostResponse
     */
    public RoomSalePostResponse createPost(RoomSalePostRequest request) {
        return marketingService.createPost(request);
    }

    /**
     * Retrieves room sale posts with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @return a PageResponse of RoomSalePostResponse
     */
    public PageResponse<RoomSalePostResponse> getPosts(int page, int size) {
        return marketingService.getPosts(page, size);
    }

    /**
     * Updates an existing room sale post by its ID.
     *
     * @param postId the post ID
     * @param request the request data
     * @return the updated RoomSalePostResponse
     */
    public RoomSalePostResponse updatePost(String postId, RoomSalePostRequest request) {
        return marketingService.updatePost(postId, request);
    }



    /**
     * Deletes a room sale post by its ID.
     *
     * @param id the post ID
     * @return a success message
     */
    public String deletePost(String id) {
        marketingService.deletePost(id);
        return "Delete Post Successfully";
    }

    /**
     * Retrieves featured room sale posts with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @return a PageResponse of RoomSalePostResponse
     */
    public PageResponse<RoomSalePostResponse> getPostsFeatured(int page, int size) {
        return marketingService.getPostsFeatured(page, size);
    }

    /**
     * Retrieves promotional room sale posts with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @return a PageResponse of RoomSalePostPromotionalResponse
     */
    public PageResponse<RoomSalePostResponse> getPostsPromotional(int page, int size) {
        return marketingService.getPostsPromotional(page, size);
    }
    /**
     * Retrieves room sale posts with pagination.
     *
     * @param page the page number
     * @param size the page size
     * @return a PageResponse of RoomSalePostPromotionalResponse
     */
    public PageResponse<RoomSalePostResponse> getPostsFilter(FilterRequest filterRequest,int page, int size) {
        return marketingService.getPostFilter(filterRequest,page, size);
    }

    /**
     * Searches for posts based on search criteria.
     *
     * @param searchRequest the search criteria
     * @return a List of RoomSalePostResponse
     */
    public PageResponse<RoomSalePostResponse> searchPosts(SearchPostRequest searchRequest, int page, int size) {
        return marketingService.searchPosts(searchRequest,page,size);
    }

    public  PageResponse<RoomSalePostResponse> getPostByUser(String status, int page,int size)
    {
        return marketingService.flitterPostWithStatusForUser(status,page, size);
    }


    public PageResponse<RoomSalePostResponse> getPostByDistrict(int district,int page,int size)
    {
        return marketingService.getPostByDistrict( district, page, size);
    }
    public  PageResponse<RoomSalePostResponse> getPostByType(int type, int page,int size)
    {
        return marketingService.filterPostWithType(type, page, size);
    }

    public InfoMarketing getInfoMarketing()
    {
        return marketingService.getInfoMarketing();
    }
}
