package com.roomfinder.marketing.controllers.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IncentiveProgramResponse {
    String id;
    String name;
    String description;
    String type;
    String startDate;
    String endDate;
    String status;
    List<PostImageResponse> postImages;
}
