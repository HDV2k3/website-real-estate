package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.exception.AppException;
import com.roomfinder.marketing.exception.ErrorCode;
import com.roomfinder.marketing.mappers.MarketingMapper;
import com.roomfinder.marketing.repositories.PromotionalRepository;
import com.roomfinder.marketing.repositories.RoomSalePostRepository;
import com.roomfinder.marketing.repositories.entities.PromotionalRoomEntity;
import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import com.roomfinder.marketing.services.impl.MarketingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MarketingServiceImplTest {

    @Autowired
    private MarketingServiceImpl marketingService;

    @MockBean
    private RoomSalePostRepository roomSalePostRepository;

    @MockBean
    private MarketingMapper marketingMapper;

    @MockBean
    private PromotionalRepository promotionalRepository;

    private String postId;
    private RoomSalePostEntity roomSalePostEntity;
    private RoomSalePostResponse roomSalePostResponse;
    private PromotionalRoomEntity promotionalRoomEntity;
    private RoomSalePostResponse promotionalRoomSalePostResponse;

    @BeforeEach
    void setUp() {
        postId = "123";

        roomSalePostEntity = RoomSalePostEntity.builder()
                .id(postId)
                .title("Room for Sale")
                .description("A spacious room in a great location")
                .build();

        roomSalePostResponse = RoomSalePostResponse.builder()
                .id(postId)
                .title("Room for Sale")
                .description("A spacious room in a great location")
                .build();

        promotionalRoomEntity = PromotionalRoomEntity.builder()
                .id("promo123")
                .roomId(postId)
                .fixPrice(new BigDecimal(10))
                .build();

        promotionalRoomSalePostResponse = RoomSalePostResponse.builder()
                .id(postId)
                .title("Promotional Room for Sale")
                .description("A spacious room in a great location with discount")
                .build();
    }

    @Test
    void getPostById_ShouldReturnRoomSalePostResponse_WhenPostExists() {
        when(roomSalePostRepository.findById(postId)).thenReturn(Optional.of(roomSalePostEntity));
        when(marketingMapper.toResponseRoomSalePost(roomSalePostEntity)).thenReturn(roomSalePostResponse);

        RoomSalePostResponse result = marketingService.getPostById(postId);

        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertEquals("Room for Sale", result.getTitle());
        assertEquals("A spacious room in a great location", result.getDescription());

        verify(roomSalePostRepository, times(1)).findById(postId);
        verify(marketingMapper, times(1)).toResponseRoomSalePost(roomSalePostEntity);
    }

    @Test
    void getPostById_ShouldThrowAppException_WhenPostDoesNotExist() {
        when(roomSalePostRepository.findById(postId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> marketingService.getPostById(postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());

        verify(roomSalePostRepository, times(1)).findById(postId);
        verifyNoInteractions(marketingMapper);
    }

    @Test
    void getProducts_ShouldReturnPageResponse_WithValidPageAndSize() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoomSalePostEntity> pageData = new PageImpl<>(List.of(roomSalePostEntity));

        when(roomSalePostRepository.findAll(pageable)).thenReturn(pageData);
        when(marketingMapper.toResponseRoomSalePost(any(RoomSalePostEntity.class))).thenReturn(roomSalePostResponse);

        var result = marketingService.getPosts(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getCurrentPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
        assertEquals(roomSalePostResponse, result.getData().get(0));

        verify(roomSalePostRepository, times(1)).findAll(pageable);
        verify(marketingMapper, times(1)).toResponseRoomSalePost(any(RoomSalePostEntity.class));
    }

    @Test
    void getRoomSalePosts_ShouldReturnPromotionalResponse_WhenRoomIsPromoted() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoomSalePostEntity> pageData = new PageImpl<>(List.of(roomSalePostEntity));

        when(roomSalePostRepository.findAll(pageable)).thenReturn(pageData);
        when(promotionalRepository.findAll()).thenReturn(List.of(promotionalRoomEntity));
        when(promotionalRepository.findByRoomId(postId)).thenReturn(Optional.of(promotionalRoomEntity));
        when(marketingMapper.toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity)).thenReturn(promotionalRoomSalePostResponse);

        var result = marketingService.getPosts(1, 10);

        assertNotNull(result);
        assertEquals(promotionalRoomSalePostResponse, result.getData().get(0));

        verify(roomSalePostRepository, times(1)).findAll(pageable);
        verify(promotionalRepository, times(1)).findAll();
        verify(marketingMapper, times(1)).toRoomSalePostPromotionalResponse(promotionalRoomEntity, roomSalePostEntity);
    }

    @Test
    void getRoomSalePosts_ShouldThrowException_WhenPageIsInvalid() {
        assertThrows(AppException.class, () -> marketingService.getPosts(0, 10));
    }

    @Test
    void getRoomSalePosts_ShouldThrowException_WhenSizeIsInvalid() {
        assertThrows(AppException.class, () -> marketingService.getPosts(1, 0));
    }

    @Test
    void getRoomSalePosts_ShouldHandleLargeValues_ForPageAndSize() {
        Pageable pageable = PageRequest.of(999998, 999999);
        Page<RoomSalePostEntity> pageData = new PageImpl<>(List.of());

        when(roomSalePostRepository.findAll(pageable)).thenReturn(pageData);
        when(promotionalRepository.findAll()).thenReturn(List.of());

        var result = marketingService.getPosts(999999, 999999);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(roomSalePostRepository, times(1)).findAll(pageable);
        verify(promotionalRepository, times(1)).findAll();
    }

    @Test
    void getRoomSalePosts_ShouldHandleNullPromotionalEntity_WhenRoomIsPromoted() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoomSalePostEntity> pageData = new PageImpl<>(List.of(roomSalePostEntity));

        when(roomSalePostRepository.findAll(pageable)).thenReturn(pageData);
        when(promotionalRepository.findAll()).thenReturn(List.of(promotionalRoomEntity));
        when(promotionalRepository.findByRoomId(postId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> marketingService.getPosts(1, 10));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }
}