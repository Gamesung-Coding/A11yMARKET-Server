package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductDetailResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.ProductResponse;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Categories;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductAiSummaryRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductImagesRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.entity.Seller;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * - search 파라미터 유무에 따라 전체/필터 조회
 * - certified/grade는 스펙 확장 시 반영
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductAiSummaryRepository productAiSummaryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(String search, Boolean certified, String grade, String categoryId) {
        Specification<Product> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("productStatus"), "APPROVED"));

            if (search != null) {
                predicates.add(builder.like(root.get("productName"), "%" + search + "%"));
            }

            Join<Product, Seller> sellerJoin = root.join("seller", JoinType.INNER);
            if (certified != null && certified) {
                predicates.add(builder.equal(sellerJoin.get("isA11yGuarantee"), true));
            }
            if (grade != null && !grade.isBlank()) {
                predicates.add(builder.equal(sellerJoin.get("sellerGrade"), grade));
            }

            Join<Product, Categories> categoryJoin = root.join("category", JoinType.INNER);
            if (categoryId != null && !categoryId.isBlank()) {
                var isCategory = builder.equal(
                        categoryJoin.get("categoryId"),
                        UUID.fromString(categoryId));
                var isParentCategory = builder.equal(
                        categoryJoin.get("parentCategory").get("categoryId"),
                        UUID.fromString(categoryId));

                predicates.add(builder.or(isCategory, isParentCategory));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };


        var list = productRepository.findAll(spec)
                .stream()
                .map(ProductResponse::fromEntity)
                .toList();

        log.debug("Filtered products count: {}", list.size());
        return list;
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(UUID productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Invalid product ID: " + productId));

        var productImages = productImagesRepository.findAllByProduct(product);
        var productAiSummary = productAiSummaryRepository.findAllByProduct(product);

        return ProductDetailResponse.fromEntity(product, productImages, productAiSummary);
    }
}