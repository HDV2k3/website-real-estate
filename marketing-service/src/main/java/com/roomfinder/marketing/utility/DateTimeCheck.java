
package com.roomfinder.marketing.utility;

import com.roomfinder.marketing.repositories.FeaturedRepository;
import com.roomfinder.marketing.repositories.entities.FeaturedRoomEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class DateTimeCheck {
    private final FeaturedRepository featuredRepository;

    @Autowired
    public DateTimeCheck(FeaturedRepository featuredRepository) {
        this.featuredRepository = featuredRepository;
    }

    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void checkAndRemoveExpiredFeatured() {
        List<FeaturedRoomEntity> featuredRooms = featuredRepository.findAll();

        for (FeaturedRoomEntity room : featuredRooms) {
            LinkedList<Integer> types = room.getTypes(); // Lấy danh sách type
            if (types == null || types.isEmpty()) {
                // Nếu danh sách types trống, xóa phòng khỏi database
                featuredRepository.delete(room);
                log.info("Removed room with empty types - RoomId: {}", room.getRoomId());
                continue;
            }

            // Kiểm tra thời gian hết hạn
            Instant currentExpiry = room.getExpiry();
            if (currentExpiry != null && currentExpiry.isBefore(Instant.now())) {
                // Loại bỏ type đầu tiên khỏi danh sách nếu hết hạn
                Integer expiredType = types.pollFirst(); // Xóa phần tử đầu tiên (hết hạn)
                log.info("Type expired and removed - RoomId: {} - Type: {} - Expired at: {}",
                        room.getRoomId(), expiredType, currentExpiry);

                // Nếu vẫn còn type, cập nhật thời gian hết hạn theo type tiếp theo
                if (!types.isEmpty()) {
                    Integer nextType = types.peekFirst(); // Lấy type tiếp theo
                    Instant newExpiry = calculateNewExpiryForType(nextType);
                    room.setExpiry(newExpiry); // Cập nhật thời gian hết hạn
                    log.info("Updated expiry for next type - RoomId: {} - Type: {} - New Expiry: {}",
                            room.getRoomId(), nextType, newExpiry);
                } else {
                    // Nếu không còn type nào, xóa phòng
                    featuredRepository.delete(room);
                    log.info("Removed room after all types expired - RoomId: {}", room.getRoomId());
                    continue;
                }

                // Lưu cập nhật vào database
                featuredRepository.save(room);

                // Gửi thông báo cho loại vừa hết hạn
                notifyFeaturedExpired(room.getRoomId(), expiredType);
            }
        }
    }

    private Instant calculateNewExpiryForType(Integer type) {
        return switch (type) {
            case 1 -> Instant.now().plusSeconds(86400); // 1 ngày
            case 2 -> Instant.now().plusSeconds(604800); // 7 ngày
            case 3 -> Instant.now().plusSeconds(2592000); // 1 tháng
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }

    private void notifyFeaturedExpired(String roomId, Integer type) {
        log.info("Notification sent for expired featured room: {} (Type: {})", roomId, type);
    }
}
