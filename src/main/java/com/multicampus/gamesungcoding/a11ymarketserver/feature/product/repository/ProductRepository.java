package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * 목록/검색용 Repository.
 * - JPQL 사용: DB 컬럼명 변경에 대한 내성 확보
 * - 문자열 연결은 CONCAT 중첩으로 '%search%' 구성 (JPQL 제약)
 */
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    // 특정 판매자의 상품 전체 조회
    Page<Product> findBySeller_SellerId(UUID sellerId, Pageable pageable);

    List<Product> findAllBySeller_User_UserEmail(String userEmail);

    int countByProductStatus(ProductStatus productStatus);

    @Query("""
            SELECT p FROM Product p
            WHERE (:query IS NULL OR
                   LOWER(p.productName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :query, '%')))
              AND (:status IS NULL OR p.productStatus = :status)
            """)
    Page<Product> findAllByQuery(String query, ProductStatus status, Pageable pageable);
}
