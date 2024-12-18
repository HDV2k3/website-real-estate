package com.roomfinder.marketing.facade;

import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.services.MarketingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketingFacadeTest {

    @Autowired
    private MarketingFacade marketingFacade; // Autowire the class being tested

    @MockBean
    private MarketingService marketingService; // Mock the MarketingService dependency

    private String postId;
    private RoomSalePostResponse roomSalePostResponse;

    @BeforeEach
    void setUp() {
        // Initialize variables before each test
        postId = "123"; // Example post ID

        // Using Builder pattern to create a RoomSalePostResponse object
        roomSalePostResponse = RoomSalePostResponse.builder()
                .id(postId)
                .title("Room for Sale")
                .description("A spacious room in a great location")
                .build();
    }

    /**
     * This test verifies that the getRoomSalePostById method returns a valid RoomSalePostResponse when the post exists.
     */
    @Test
    void getRoomSalePostById_ShouldReturnRoomSalePostResponse_WhenPostExists() {
        // Arrange: Set up mock behavior to return a valid RoomSalePostResponse
        when(marketingService.getPostById(postId)).thenReturn(roomSalePostResponse);

        // Act: Call the method under test
        RoomSalePostResponse result = marketingFacade.getRoomSalePostById(postId);

        // Assert: Validate the result
        assertNotNull(result); // Ensure the result is not null
        assertEquals(postId, result.getId()); // Verify the post ID matches
        assertEquals("Room for Sale", result.getTitle()); // Verify the post title
        assertEquals("A spacious room in a great location", result.getDescription()); // Verify the post description

        // Verify that the MarketingService method was called exactly once with the correct post ID
        verify(marketingService, times(1)).getPostById(postId);
    }

    /**
     * This test verifies that the getRoomSalePostById method returns null when the post does not exist.
     */
    @Test
    void getRoomSalePostById_ShouldReturnNull_WhenPostDoesNotExist() {
        // Arrange: Mock behavior to return null when the post is not found
        when(marketingService.getPostById(postId)).thenReturn(null);

        // Act: Call the method under test
        RoomSalePostResponse result = marketingFacade.getRoomSalePostById(postId);

        // Assert: Validate the result is null
        assertNull(result);

        // Verify that the MarketingService method was called exactly once with the correct post ID
        verify(marketingService, times(1)).getPostById(postId);
    }
}