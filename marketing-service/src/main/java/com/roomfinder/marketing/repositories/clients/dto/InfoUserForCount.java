package com.roomfinder.marketing.repositories.clients.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoUserForCount {
    Integer quantityUser;
    List<Integer> id;

}
