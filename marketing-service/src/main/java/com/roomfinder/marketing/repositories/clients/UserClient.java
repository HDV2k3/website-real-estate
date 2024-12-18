package com.roomfinder.marketing.repositories.clients;

import com.roomfinder.marketing.configuration.security.AuthenticationRequestInterceptor;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.repositories.clients.dto.InfoUserForCount;
import com.roomfinder.marketing.repositories.clients.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user",url = "${service.url.user}", configuration = {AuthenticationRequestInterceptor.class})
public interface UserClient {
    @GetMapping(value = "/users/my-info",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<UserResponse> getMyInfo();
    @GetMapping("/api/v1/subscriptions/check-post-permission")
    GenericApiResponse<Boolean> canCreatePost(@RequestHeader("userId") Integer userId);

    @PostMapping("/api/v1/subscriptions/decrement-free-post")
    GenericApiResponse<Void> decrementFreePost(@RequestHeader("userId") Integer userId);

    @GetMapping(value = "users/quantityUser",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<InfoUserForCount> countUser();

}
