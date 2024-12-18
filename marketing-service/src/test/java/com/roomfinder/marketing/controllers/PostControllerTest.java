package com.roomfinder.marketing.controllers;

import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;
import com.roomfinder.marketing.facade.MarketingFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc to simulate HTTP requests

    @MockBean
    private MarketingFacade marketingFacade; // Mock the MarketingFacade dependency

    private String postId;
    private RoomSalePostResponse roomSalePostResponse;

    @BeforeEach
    void setUp() {
        // Set up mock data and initialize variables before each test case

        // Example post ID for testing
        postId = "123";

        // Using the Builder pattern to create a RoomSalePostResponse object
        roomSalePostResponse = RoomSalePostResponse.builder().id(postId).title("Room for Sale").description("A spacious room in a great location").build();
    }

    /**
     * This test checks if the correct response is returned when a post exists.
     * It mocks the MarketingFacade to return a RoomSalePostResponse when the post is found.
     * It then performs a GET request to verify that the correct post data is returned in the response.
     */
    @Test
    void getRoomSalePostById_ShouldReturnSuccessResponse_WhenPostExists() throws Exception {
        // Arrange: Set up mock behavior to return a valid post
        when(marketingFacade.getRoomSalePostById(postId)).thenReturn(roomSalePostResponse);

        // Act: Perform the GET request using MockMvc
        mockMvc.perform(get("/post/post-by-id/{id}", postId).contentType(MediaType.APPLICATION_JSON))
                // Assert: Verify the HTTP status, content type, and response body
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.data.id").value(postId)) // Verify that the post ID is correct
                .andExpect(jsonPath("$.data.title").value("Room for Sale")) // Verify the post title
                .andExpect(jsonPath("$.data.description").value("A spacious room in a great location")); // Verify the post description

        // Verify that the MarketingFacade method was called exactly once with the correct post ID
        verify(marketingFacade, times(1)).getRoomSalePostById(postId);
    }

    /**
     * This test checks if the correct error response is returned when a post does not exist.
     * It mocks the MarketingFacade to return null when the post is not found.
     * It then performs a GET request and verifies that the response contains an appropriate error message.
     */
    @Test
    void getRoomSalePostById_ShouldReturnErrorResponse_WhenPostDoesNotExist() throws Exception {
        // Arrange: Mock behavior to return null for a non-existent post
        when(marketingFacade.getRoomSalePostById(postId)).thenReturn(null);

        // Act: Perform the GET request using MockMvc
        mockMvc.perform(get("/post/post-by-id/{id}", postId).contentType(MediaType.APPLICATION_JSON))
                // Assert: Verify the HTTP status, content type, and response body
                .andExpect(status().isOk()) // Expect HTTP 200 OK since the API responds successfully even if no post is found
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.responseCode").value(101000))  // Verify that the correct response code is returned
                .andExpect(jsonPath("$.data").doesNotExist())  // Verify that the data field is not present (or null)
                .andExpect(jsonPath("$.message").value("Success"));  // Verify that the message indicates success, even when no data is found

        // Verify that the MarketingFacade method was called exactly once with the correct post ID
        verify(marketingFacade, times(1)).getRoomSalePostById(postId);
    }
}
