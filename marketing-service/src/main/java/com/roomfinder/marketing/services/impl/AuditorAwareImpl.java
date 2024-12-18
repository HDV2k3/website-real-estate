package com.roomfinder.marketing.services.impl;

import com.roomfinder.marketing.repositories.UserRepository;
import com.roomfinder.marketing.utility.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuditorAwareImpl implements AuditorAware<String> {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//        var user = userRepository.getMyInfo();
//        String fullName = user.getFirstName()+ " " + user.getLastName();
//        return Optional.of(fullName);
//    }
//}
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final UserRepository userRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        try {
            var user = userRepository.getMyInfo();
            String fullName = user.getFirstName() + " " + user.getLastName();

            // Set a thread-local or use a context holder to store the current user ID
            UserContextHolder.setCurrentUserId(user.getId());

            return Optional.of(fullName);
        } catch (Exception e) {
            log.error("Error retrieving current auditor", e);
            return Optional.empty();
        }
    }
}