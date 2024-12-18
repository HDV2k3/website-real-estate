package com.roomfinder.marketing.controllers.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseIndexRequest {
    String roomId;
    Integer index;
}
