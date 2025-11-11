package com.multicampus.gamesungcoding.a11ymarketserver.address.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String addressId;
    private String userId;
    private String receiverName;
    private String receiverPhone;
    private Integer receiverZipcode;
    private String receiverAddr1;
    private String receiverAddr2;
    private Date createdAt;

    public static AddressDTO fromEntity(Address address) {
        return AddressDTO.builder()
                .addressId(address.getAddressId())
                .userId(address.getUserId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .receiverZipcode(address.getReceiverZipcode())
                .receiverAddr1(address.getReceiverAddr1())
                .receiverAddr2(address.getReceiverAddr2())
                .createdAt(address.getCreatedAt())
                .build();
    }

    public Address toEntity() {
        return Address.builder()
                .addressId(this.addressId)
                .userId(this.userId)
                .receiverName(this.receiverName)
                .receiverPhone(this.receiverPhone)
                .receiverZipcode(this.receiverZipcode)
                .receiverAddr1(this.receiverAddr1)
                .receiverAddr2(this.receiverAddr2)
                .createdAt(this.createdAt)
                .build();
    }

}
