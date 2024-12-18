package com.roomfinder.marketing.services.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetTimeExpiry {
    public String formatRemainingTime(long seconds) {
        if (seconds < 60) {
            return seconds + " giây";
        }
        long minutes = seconds / 60;
        seconds = seconds % 60;
        if (minutes < 60) {
            return String.format("%d phút %d giây", minutes, seconds);
        }
        long hours = minutes / 60;
        minutes = minutes % 60;
        if (hours < 24) {
            return String.format("%d giờ %d phút", hours, minutes);
        }
        long days = hours / 24;
        hours = hours % 24;
        return String.format("%d ngày %d giờ", days, hours);
    }
}
