package com.roomfinder.marketing.repositories.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PricingDetail {

    @Field("basePrice")
    @NotNull
    @Positive
    BigDecimal basePrice; // Base price for renting the room

    @Field("electricityCost")
    @NotNull
    @Positive
    BigDecimal electricityCost; // Cost for electricity service

    @Field("waterCost")
    @NotNull
    @Positive
    BigDecimal waterCost; // Cost for water service
    @Field("additionalFees")
    List<FeeDetail> additionalFees;
}
