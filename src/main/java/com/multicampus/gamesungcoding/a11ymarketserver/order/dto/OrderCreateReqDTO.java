package com.multicampus.gamesungcoding.a11ymarketserver.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateReqDTO {

    @NotNull
    private UUID addressId;

    @NotNull
    private List<OrderItemReqDTO> orderItems;

}
