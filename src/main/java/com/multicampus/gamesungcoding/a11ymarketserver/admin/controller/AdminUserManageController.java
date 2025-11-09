package com.multicampus.gamesungcoding.a11ymarketserver.admin.controller;

import com.multicampus.gamesungcoding.a11ymarketserver.admin.service.AdminUserManageService;
import com.multicampus.gamesungcoding.a11ymarketserver.user.model.UserAdminDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminUserManageController {
    private final AdminUserManageService userService;

    // 관리자 - 전체 사용자 조회
    @GetMapping("/v1/admin/users")
    public ResponseEntity<List<UserAdminDTO>> inquireUsers() {
        log.info("AdminUserManageController - inquireUsers");

        return ResponseEntity.ok(userService.listAll());
    }

    // 관리자 - 사용자 권한 변경
    @PatchMapping("/v1/admin/users/{userId}/permission")
    public ResponseEntity<String> changeUserPermission(@PathVariable String userId, @RequestParam String role) {
        log.info("AdminUserManageController - changeUserPermission");

        return ResponseEntity.ok(userService.changePermission(userId, role));
    }

    // 관리자 - 판매자 승인 대기 목록 조회 (미구현)
    @GetMapping("/v1/admin/sellers/pending")
    public ResponseEntity<String> inquirePendingSellers() {
        log.info("AdminUserManageController - inquirePendingSellers");

        // Placeholder for future implementation
        return ResponseEntity.ok("Pending sellers inquiry functionality is under development.");
    }

    // 관리자 - 판매자 상태 변경 (미구현)
    @PatchMapping("/v1/admin/sellers/{sellerId}/status")
    public ResponseEntity<String> changeSellerStatus(@PathVariable String sellerId, @RequestParam String status) {
        log.info("AdminUserManageController - changeSellerStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change seller status functionality is under development.");
    }

    // 관리자 - 판매자 정보 수정 (미구현)
    @PatchMapping("/v1/admin/sellers/{sellerId}")
    public ResponseEntity<String> updateSellerInfo(@PathVariable String sellerId, @RequestBody String sellerInfo) {
        log.info("AdminUserManageController - updateSellerInfo");

        // Placeholder for future implementation
        return ResponseEntity.ok("Update seller information functionality is under development.");
    }

    // 관리자 - 상품 승인 대기 목록 조회 (미구현)
    @GetMapping("/v1/admin/products/pending")
    public ResponseEntity<String> inquirePendingProducts() {
        log.info("AdminUserManageController - inquirePendingProducts");

        // Placeholder for future implementation
        return ResponseEntity.ok("Pending products inquiry functionality is under development.");
    }

    // 관리자 - 상품 상태 변경 (미구현)
    @PatchMapping("/v1/admin/products/{productId}/status")
    public ResponseEntity<String> changeProductStatus(@PathVariable String productId, @RequestParam String status) {
        log.info("AdminUserManageController - changeProductStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change product status functionality is under development.");
    }

    // 관리자 - 전체 주문 조회 (미구현)
    @GetMapping("/v1/admin/orders")
    public ResponseEntity<String> inquireAllOrders() {
        log.info("AdminUserManageController - inquireAllOrders");

        // Placeholder for future implementation
        return ResponseEntity.ok("All orders inquiry functionality is under development.");
    }

    // 관리자 - 특정 주문 조회 (미구현)
    @GetMapping("/v1/admin/orders/{orderId}")
    public ResponseEntity<String> inquireOrderDetails(@PathVariable String orderId) {
        log.info("AdminUserManageController - inquireOrderDetails");

        // Placeholder for future implementation
        return ResponseEntity.ok("Order details inquiry functionality is under development.");
    }

    // 관리자 - 주문 상태 변경 (미구현)
    @PatchMapping("/v1/admin/orders/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        log.info("AdminUserManageController - changeOrderStatus");

        // Placeholder for future implementation
        return ResponseEntity.ok("Change order status functionality is under development.");
    }
}
