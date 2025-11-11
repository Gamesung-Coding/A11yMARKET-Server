package com.multicampus.gamesungcoding.a11ymarketserver.address.service;

import com.multicampus.gamesungcoding.a11ymarketserver.address.model.Address;
import com.multicampus.gamesungcoding.a11ymarketserver.address.model.AddressDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.address.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    // 배송지 목록 조회
    @Override
    public List<AddressDTO> getAddressList(String userId) {
        return addressRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AddressDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Override
    @Transactional
    public AddressDTO insertAddress(String userId, AddressDTO addressDTO) {

        String addressId = UUID.randomUUID().toString();

        Address address = Address.builder()
                .addressId(addressId)
                .userId(userId)
                .receiverName(addressDTO.getReceiverName())
                .receiverPhone(addressDTO.getReceiverPhone())
                .receiverZipcode(addressDTO.getReceiverZipcode())
                .receiverAddr1(addressDTO.getReceiverAddr1())
                .receiverAddr2(addressDTO.getReceiverAddr2())
                .createdAt(new Date())
                .build();

        return AddressDTO.fromEntity(addressRepository.save(address));
    }

    // 배송지 수정
    @Override
    @Transactional
    public AddressDTO updateAddress(String userId, String addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findByAddressIdAndUserId(addressId, userId)
                .orElseThrow();

        address.updateAddrInfo(
                addressDTO.getReceiverName(),
                addressDTO.getReceiverPhone(),
                addressDTO.getReceiverZipcode(),
                addressDTO.getReceiverAddr1(),
                addressDTO.getReceiverAddr2()
        );

        return AddressDTO.fromEntity(address);
    }

    // 배송지 삭제
    @Override
    @Transactional
    public void deleteAddress(String userId, String addressId) {
        Address address = addressRepository.findByAddressIdAndUserId(addressId, userId)
                .orElseThrow();

        addressRepository.delete(address);
    }
}
