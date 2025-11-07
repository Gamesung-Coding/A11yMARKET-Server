package com.multicampus.gamesungcoding.a11ymarketserver.product.repository;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * [ProductRepository]
 * - 상품 목록 조회 및 조건 검색 Repository
 * - 현재: Product 테이블만 사용 (SELLER DB 조인은 추후 추가 예정)
 */
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * [상품 조건 검색]
     * - search: 상품명 LIKE 검색 (현재 기능)
     * - certified, grade: 나중에 Seller DB와 조인 후 활성화 예정
     */
    @Query(value = """
            SELECT p.*
            FROM products p
            /*
               [TODO] SELLER DB와 JOIN 예정
               예시)
               JOIN seller s ON s.seller_id = p.seller_id
               AND (:certified IS NULL OR s.accessibility_certified = CASE WHEN :certified THEN 1 ELSE 0 END)
               AND (:grade IS NULL OR s.grade = :grade)
            */
            WHERE (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
            """, nativeQuery = true)
    List<Product> findFilteredProducts(
            @Param("search") String search,
            @Param("certified") Boolean certified,
            @Param("grade") String grade
    );
}
