package com.multicampus.gamesungcoding.a11ymarketserver.product.service;

import com.multicampus.gamesungcoding.a11ymarketserver.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * [ProductServiceImpl]
 * - 상품 목록 조회 및 조건 검색 구현체
 * - 현재는 Product 테이블만 활용 (SELLER DB 조인은 추후 예정)
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * [전체 상품 목록 조회]
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * [상품 조건 검색]
     * - search / certified / grade 중 값이 있는 항목만 조건에 포함
     * - 현재는 search만 실제 필터링 동작
     * - certified, grade는 나중에 Seller DB와 조인 시 쿼리 수정으로 반영 예정
     */
    @Override
    public List<ProductDTO> getFilteredProducts(String search, Boolean certified, String grade) {

        boolean hasSearch = (search != null && !search.isBlank());
        boolean hasCertified = (certified != null);
        boolean hasGrade = (grade != null && !grade.isBlank());

        if (!hasSearch && !hasCertified && !hasGrade) {
            // 조건이 전혀 없으면 전체 조회
            return getAllProducts();
        }

        // Repository 호출 (현재 search만 반영)
        List<Product> products = productRepository.findFilteredProducts(
                hasSearch ? search : null,
                hasCertified ? certified : null,
                hasGrade ? grade : null
        );

        return products.stream()
                .map(ProductDTO::from)
                .collect(Collectors.toList());
    }
}
