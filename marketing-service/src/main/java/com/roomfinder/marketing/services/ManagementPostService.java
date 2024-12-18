package com.roomfinder.marketing.services;

import com.roomfinder.marketing.controllers.dto.response.RoomSalePostResponse;

public interface ManagementPostService {
    RoomSalePostResponse getAllPostIsShowing(int userId);
    RoomSalePostResponse getAllPostExpired(int userId);
    RoomSalePostResponse getAllPostCancel(int userId);
    RoomSalePostResponse getAllPostPending(int userId);
}
