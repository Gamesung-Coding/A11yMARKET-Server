package com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.service;

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataDuplicatedException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.InvalidRequestException;
import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.UserNotFoundException;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.Product;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductDTO;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.model.ProductStatus;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.ProductRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.model.*;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.seller.repository.SellerRepository;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.model.Users;
import com.multicampus.gamesungcoding.a11ymarketserver.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SellerApplyResponse applySeller(String userEmail, SellerApplyRequest request) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 존재하지 않습니다."));

        sellerRepository.findByUserId(user.getUserId()).ifPresent(existing -> {
            throw new DataDuplicatedException("이미 판매자이거나 판매자 신청 이력이 존재합니다.");
        });

        Seller seller = Seller.builder()
                .userId(user.getUserId())
                .sellerName(request.sellerName())
                .businessNumber(request.businessNumber())
                .sellerGrade(SellerGrades.NEWER.getGrade())
                .sellerIntro(request.sellerIntro())
                .a11yGuarantee(false)
                .sellerSubmitStatus(SellerSubmitStatus.PENDING.name())
                .build();

        Seller saved = sellerRepository.save(seller);

        return new SellerApplyResponse(
                saved.getSellerId(),
                saved.getSellerName(),
                saved.getBusinessNumber(),
                saved.getSellerGrade(),
                saved.getSellerIntro(),
                saved.getA11yGuarantee(),
                saved.getSellerSubmitStatus(),
                saved.getSubmitDate(),
                saved.getApprovedDate());
    }

    public ProductDTO registerProduct(String userEmail, SellerProductRegisterRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보가 존재하지 않습니다. 먼저 판매자 가입 신청을 완료하세요."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품 등록이 가능합니다.");
        }

        UUID sellerId = seller.getSellerId();
        UUID categoryId = UUID.fromString(request.categoryId());

        Product product = Product.builder()
                .sellerId(sellerId)
                .categoryId(categoryId)
                .productName(request.productName())
                .productDescription(request.productDescription())
                .productPrice(request.productPrice())
                .productStock(request.productStock())
                .productStatus(ProductStatus.PENDING)
                .build();

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getMyProducts(String userEmail) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        UUID sellerId = seller.getSellerId();

        List<Product> products = productRepository.findBySellerId(sellerId);

        return products.stream().map(ProductDTO::fromEntity).toList();
    }

    @Transactional
    public ProductDTO updateProduct(String userEmail, UUID productId, SellerProductUpdateRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 수정할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 수정할 수 있습니다.");
        }

        product.updateBySeller(
                UUID.fromString(request.categoryId()),
                request.productName(),
                request.productDescription(),
                request.productPrice(),
                request.productStock()
        );

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(String userEmail, UUID productId) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 상품을 삭제할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품만 삭제할 수 있습니다.");
        }

        product.deleteBySeller();

        productRepository.save(product);
    }

    @Transactional
    public ProductDTO updateProductStock(String userEmail, UUID productId, SellerProductStockUpdateRequest request) {

        Seller seller = sellerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new DataNotFoundException("판매자 정보를 찾을 수 없습니다."));

        if (!seller.getSellerSubmitStatus().equals(SellerSubmitStatus.APPROVED.getStatus())) {
            throw new InvalidRequestException("판매자 승인 완료 후 재고를 수정할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("상품 정보를 찾을 수 없습니다."));

        if (!product.getSellerId().equals(seller.getSellerId())) {
            throw new InvalidRequestException("본인의 상품 재고만 수정할 수 있습니다.");
        }

        product.updateStockBySeller(request.productStock());

        return ProductDTO.fromEntity(productRepository.save(product));
    }
}