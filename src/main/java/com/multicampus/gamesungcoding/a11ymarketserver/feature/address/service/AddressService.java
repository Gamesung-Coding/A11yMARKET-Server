package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.DefaultAddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.entity.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final DefaultAddressRepository defaultAddressRepository;
    private final UserRepository userRepository;

    // 배송지 목록 조회
    public List<AddressResponse> getAddressList(String userEmail) {
        return addressRepository.findByUserOrderByCreatedAtDesc(
                        getUserByEmail(userEmail)
                )
                .stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressResponse insertAddress(String userEmail, AddressRequest dto) {

        Addresses address = Addresses.builder()
                .user(getUserByEmail(userEmail))
                .addressInfo(
                        AddressInfo.builder()
                                .addressName(dto.addressName())
                                .receiverName(dto.receiverName())
                                .receiverPhone(dto.receiverPhone())
                                .receiverZipcode(dto.receiverZipcode())
                                .receiverAddr1(dto.receiverAddr1())
                                .receiverAddr2(dto.receiverAddr2())
                                .build()
                )
                .build();

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    // 배송지 수정
    @Transactional
    public AddressResponse updateAddress(String userEmail, String addressId, AddressRequest dto) {
        // 사용자 소유의 주소인지 확인
        addressRepository.findByAddressIdAndUser_UserId(
                        UUID.fromString(addressId),
                        getUserByEmail(userEmail).getUserId())
                .orElseThrow(() -> new DataNotFoundException("Address not found for user"));

        Addresses address = addressRepository.findById(UUID.fromString(addressId))
                .orElseThrow(() -> new DataNotFoundException("Address not found"));

        address.updateAddrInfo(
                AddressInfo.builder()
                        .addressName(dto.addressName())
                        .receiverName(dto.receiverName())
                        .receiverPhone(dto.receiverPhone())
                        .receiverZipcode(dto.receiverZipcode())
                        .receiverAddr1(dto.receiverAddr1())
                        .receiverAddr2(dto.receiverAddr2())
                        .build()
        );
        return AddressResponse.fromEntity(address);
    }

    // 배송지 삭제
    @Transactional
    public void deleteAddress(String userEmail, String addressId) {
        addressRepository.findByAddressIdAndUser_UserId(
                        UUID.fromString(addressId),
                        getUserByEmail(userEmail).getUserId()
                )
                .ifPresent(addressRepository::delete);
    }

    /**
     * Default_Address
     */

    // 기본 배송지 조회
    public AddressResponse getDefaultAddress(String userEmail) {
        return defaultAddressRepository.findByUser_UserId(getUserByEmail(userEmail).getUserId())
                .flatMap(defaultAddr -> addressRepository.findById(defaultAddr.getAddressId()))
                .map(AddressResponse::fromEntity)
                .orElseThrow(() -> new DataNotFoundException("Default address not found"));
    }

    // 기본 배송지 변경
    @Transactional
    public void setDefaultAddress(String userEmail, UUID addressId) {
        Users user = getUserByEmail(userEmail);

        // 사용자의 기본 배송지 정보가 존재하는지 확인 후 업데이트 또는 생성
        defaultAddressRepository.findById(user.getUserId())
                .ifPresentOrElse(
                        defaultAddress -> defaultAddress.changeDefaultAddress(addressId),
                        () -> defaultAddressRepository.save(DefaultAddress.builder()
                                .user(user)
                                .addressId(addressId)
                                .build())
                );
    }

    private Users getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

}