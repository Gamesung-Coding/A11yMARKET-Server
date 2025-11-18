package com.multicampus.gamesungcoding.a11ymarketserver.feature.address.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.AddressResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.model.DefaultAddressRequest;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    // 배송지 목록 조회
    @GetMapping("/v1/users/me/address")
    public ResponseEntity<List<AddressResponse>> getAddressList(
            @AuthenticationPrincipal Authentication authentication) {
        // UUID userId = UUID.fromString(uuid);
        List<AddressResponse> addresses = addressService.getAddressList(authentication.getName());
        return ResponseEntity.ok(addresses);
    }

    // 배송지 등록
    @PostMapping("/v1/users/me/address")
    public ResponseEntity<AddressResponse> insertAddress(
            @AuthenticationPrincipal Authentication authentication,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response = addressService.insertAddress(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 배송지 정보 수정
    @PutMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal Authentication authentication,
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.updateAddress(authentication.getName(), addressId, request);
        return ResponseEntity.ok(response);
    }

    // 배송지 삭제
    @DeleteMapping("/v1/users/me/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal Authentication authentication,
            @PathVariable String addressId) {

        addressService.deleteAddress(authentication.getName(), addressId);
        return ResponseEntity.noContent().build();
    }

    // 기본 배송지 조회
    @GetMapping("/v1/users/me/default-address")
    public ResponseEntity<AddressResponse> getDefaultAddress(
            @AuthenticationPrincipal Authentication authentication) {
        // 기본 배송지가 없을 때 204 반환
        var address = addressService.getDefaultAddress(authentication.getName());
        return ResponseEntity.ok(address);
    }

    // 기본 배송지 변경
    @PatchMapping("/v1/users/me/default-address")
    public ResponseEntity<String> updateDefaultAddress(
            @AuthenticationPrincipal Authentication authentication,
            @Valid @RequestBody DefaultAddressRequest request) {

        addressService.setDefaultAddress(authentication.getName(), request);
        return ResponseEntity.ok("SUCCESS");
    }

}
