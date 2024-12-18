package com.roomfinder.marketing.services.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
@Slf4j
@Component
public class CalculateExpiry {
    public Instant calculateExpiryFeatured(Integer type) {
        return switch (type) {
            case 1 -> Instant.now().plus(1, ChronoUnit.DAYS);
            case 2 -> Instant.now().plus(7, ChronoUnit.DAYS);
            case 3 -> Instant.now().plus(1, ChronoUnit.MONTHS);
            default -> {
                log.error("Invalid featured type attempted: {}", type);
                throw new IllegalArgumentException("Invalid featured type: " + type);
            }
        };
    }
    /**
     * Hàm tính toán thời gian hết hạn dựa trên typePackage.
     */
    public Instant calculateExpiryFromType(int typePackage, Instant baseExpiry) {
        return switch (typePackage) {
            case 1 -> baseExpiry.plus(1, ChronoUnit.DAYS);
            case 2 -> baseExpiry.plus(7, ChronoUnit.DAYS);
            case 3 -> baseExpiry.plus(1, ChronoUnit.MONTHS);
            default -> throw new IllegalArgumentException("Invalid featured type: " + typePackage);
        };
    }
}
