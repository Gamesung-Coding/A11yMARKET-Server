package com.multicampus.gamesungcoding.a11ymarketserver.product.model;

import lombok.*;

/**
 * [ProductDTO]
 * - Entity → View 계층 데이터 전달용
 * - 인증 여부, 등급 필드는 Seller DB 조인 후 확장 예정
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String id;
    private String name;
    private String category;
    private Integer price;
    private String status;

    /**
     * 엔티티를 DTO로 변환하는 정적 메서드
     */
    public static ProductDTO from(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .status(product.getStatus())
                // .certified(product.getSeller().getCertified())  // Seller 엔티티 조인 후
                // .grade(product.getSeller().getGrade())          // Seller 엔티티 조인 후
                .build();
    }
}
