package com.roomfinder.marketing.utility;

public class UserContextHolder {
    private static final ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

    public static void setCurrentUserId(Integer userId) {
        currentUserId.set(userId);
    }

    public static Integer getCurrentUserId() {
        return currentUserId.get();
    }

    public static void clear() {
        currentUserId.remove();
    }
}