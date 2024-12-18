package com.roomfinder.marketing.repositories.clients;

import com.roomfinder.marketing.configuration.security.AuthenticationRequestInterceptor;
import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment",url = "${service.url.payment}", configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @GetMapping(value = "/userPayment/minusBalance",produces = MediaType.APPLICATION_JSON_VALUE)
    GenericApiResponse<String> minusBalance(@RequestParam int type,@RequestParam String roomId);
}
