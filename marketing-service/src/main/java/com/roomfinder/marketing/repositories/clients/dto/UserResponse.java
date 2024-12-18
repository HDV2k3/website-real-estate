package com.roomfinder.marketing.repositories.clients.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String email;
    String firstName;
    String lastName;
    LocalDate dayOfBirth;
    String verificationToken;
    boolean enabled;
    Set<RoleResponse> roles;
}
