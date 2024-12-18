package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.repositories.entities.RoomSalePostEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest // Use @DataMongoTest to test MongoDB repositories
@AutoConfigureDataMongo
class RoomSalePostRepositoryTest {

    @Autowired
    private RoomSalePostRepository roomSalePostRepository; // Autowire the repository

    private RoomSalePostEntity roomSalePostEntity1;
    private RoomSalePostEntity roomSalePostEntity2;
    private String roomId;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        roomId = "room123";

        // Create and save two RoomSalePostEntity objects for testing
        roomSalePostEntity1 = RoomSalePostEntity.builder()
                .id("post1")
                .roomId(roomId)
                .title("Room 1")
                .description("Spacious room")
                .build();

        roomSalePostEntity2 = RoomSalePostEntity.builder()
                .id("post2")
                .roomId(roomId)
                .title("Room 2")
                .description("Another spacious room")
                .build();

        roomSalePostRepository.save(roomSalePostEntity1); // Save entity 1
        roomSalePostRepository.save(roomSalePostEntity2); // Save entity 2
    }

    /**
     * This test verifies that findAllByRoomId returns all posts associated with the given roomId.
     */
    @Test
    void findAllByRoomId_ShouldReturnListOfPosts_WhenPostsExist() {
        // Act: Call the repository method to fetch posts by roomId
        List<RoomSalePostEntity> posts = roomSalePostRepository.findAllByRoomId(roomId);

        // Assert: Check that the list contains exactly 2 posts
        assertNotNull(posts); // Ensure the result is not null
        assertEquals(2, posts.size()); // Verify the correct number of posts are returned
        assertTrue(posts.stream().anyMatch(post -> post.getId().equals("post1"))); // Verify post1 is returned
        assertTrue(posts.stream().anyMatch(post -> post.getId().equals("post2"))); // Verify post2 is returned
    }

    /**
     * This test verifies that findAllByRoomId returns an empty list when no posts are associated with the given roomId.
     */
    @Test
    void findAllByRoomId_ShouldReturnEmptyList_WhenNoPostsExist() {
        // Act: Call the repository method with a roomId that has no posts
        List<RoomSalePostEntity> posts = roomSalePostRepository.findAllByRoomId("nonExistentRoomId");

        // Assert: Ensure the result is an empty list
        assertNotNull(posts); // Ensure the result is not null
        assertTrue(posts.isEmpty()); // Verify the result is empty
    }

    /**
     * This test verifies that findById fetches the correct post when a valid postId is provided.
     */
    @Test
    void findById_ShouldReturnPost_WhenPostExists() {
        // Act: Fetch a post by its ID
        var post = roomSalePostRepository.findById("post1");

        // Assert: Ensure the post is found and matches the expected data
        assertTrue(post.isPresent()); // Ensure the post is present
        assertEquals("Room 1", post.get().getTitle()); // Verify the title
        assertEquals("Spacious room", post.get().getDescription()); // Verify the description
    }

    /**
     * This test verifies that findById returns empty when the post does not exist.
     */
    @Test
    void findById_ShouldReturnEmpty_WhenPostDoesNotExist() {
        // Act: Fetch a post by a non-existent ID
        var post = roomSalePostRepository.findById("nonExistentPostId");

        // Assert: Ensure the result is empty
        assertTrue(post.isEmpty()); // Verify no post is found
    }

    /**
     * Clean up the database after each test.
     * This ensures that each test is isolated and doesn't leave residual data.
     */
    @AfterEach
    void tearDown() {
        // Clean up: Remove all records from the repository to ensure isolation
        roomSalePostRepository.deleteAll();
    }
}