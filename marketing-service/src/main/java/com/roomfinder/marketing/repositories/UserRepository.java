package com.roomfinder.marketing.repositories;

import com.roomfinder.marketing.controllers.model.GenericApiResponse;
import com.roomfinder.marketing.repositories.clients.UserClient;
import com.roomfinder.marketing.repositories.clients.dto.InfoUserForCount;
import com.roomfinder.marketing.repositories.clients.dto.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserRepository {
    UserClient userClient;

    public UserResponse getMyInfo() {
        UserResponse result = null;
        GenericApiResponse<UserResponse> clientResponse = userClient.getMyInfo();

        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
    public InfoUserForCount quantityUser() {
        InfoUserForCount result = null;
        GenericApiResponse<InfoUserForCount> clientResponse = userClient.countUser();

        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
}
