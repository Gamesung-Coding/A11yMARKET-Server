package com.multicampus.gamesungcoding.a11ymarketserver.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemReqDTO {

    @NotNull
    private UUID productId;

    @Min(1)
    private Integer quantity;
}
