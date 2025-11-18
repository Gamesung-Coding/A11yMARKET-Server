package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.AddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.repository.DefaultAddressRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final DefaultAddressRepository defaultAddressRepository;
    private final UserRepository userRepository;

    // 배송지 목록 조회
    public List<AddressResponse> getAddressList(String userEmail) {
        return addressRepository.findByUserIdOrderByCreatedAtDesc(
                        getUserIdByEmail(userEmail)
                )
                .stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressResponse insertAddress(String userEmail, AddressRequest dto) {

        Addresses address = Addresses.builder()
                .userId(getUserIdByEmail(userEmail))
                .addressName(dto.getAddressName())
                .receiverName(dto.getReceiverName())
                .receiverPhone(dto.getReceiverPhone())
                .receiverZipcode(dto.getReceiverZipcode())
                .receiverAddr1(dto.getReceiverAddr1())
                .receiverAddr2(dto.getReceiverAddr2())
                .build();

        return AddressResponse.fromEntity(addressRepository.save(address));
    }

    // 배송지 수정
    @Transactional
    public AddressResponse updateAddress(String userEmail, String addressId, AddressRequest dto) {
        // 사용자 소유의 주소인지 확인
        addressRepository.findByAddressIdAndUserId(UUID.fromString(addressId), getUserIdByEmail(userEmail))
                .orElseThrow(() -> new EntityNotFoundException("Address not found for user"));

        Addresses address = addressRepository.findById(UUID.fromString(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        address.updateAddrInfo(
                dto.getAddressName(),
                dto.getReceiverName(),
                dto.getReceiverPhone(),
                dto.getReceiverZipcode(),
                dto.getReceiverAddr1(),
                dto.getReceiverAddr2()
        );
        return AddressResponse.fromEntity(address);
    }

    // 배송지 삭제
    @Transactional
    public void deleteAddress(String userEmail, String addressId) {
        addressRepository.findByAddressIdAndUserId(
                        UUID.fromString(addressId),
                        getUserIdByEmail(userEmail)
                )
                .ifPresent(addressRepository::delete);
    }

    /**
     * Default_Address
     */

    // 기본 배송지 조회
    public AddressResponse getDefaultAddress(String userEmail) {
        return defaultAddressRepository.findByUserId(getUserIdByEmail(userEmail))
                .flatMap(defaultAddr -> addressRepository.findById(defaultAddr.getAddressId()))
                .map(AddressResponse::fromEntity)
                .orElseThrow(() -> new DataNotFoundException("Default address not found"));
    }

    // 기본 배송지 변경
    @Transactional
    public void setDefaultAddress(String userEmail, DefaultAddressRequest request) {
        UUID userId = getUserIdByEmail(userEmail);

        // 기본 배송지 조회
        Optional<DefaultAddress> defaultOpt =
                defaultAddressRepository.findById(userId);

        if (defaultOpt.isPresent()) {
            var defaultAddress = defaultOpt.get();
            defaultAddress.changeDefaultAddress(request.getAddressId());
        } else {
            var defaultAddress = DefaultAddress.builder()
                    .userId(userId)
                    .addressId(request.getAddressId())
                    .build();
            defaultAddressRepository.save(defaultAddress);
        }
    }

    private UUID getUserIdByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getUserId();
    }

}