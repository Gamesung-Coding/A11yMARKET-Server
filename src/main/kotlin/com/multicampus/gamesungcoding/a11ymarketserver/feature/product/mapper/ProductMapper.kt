package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper

import com.multicampus.gamesungcoding.a11ymarketserver.common.exception.DataNotFoundException
import com.multicampus.gamesungcoding.a11ymarketserver.feature.cart.dto.CartItemDto
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.*
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.Product
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductAiSummary
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.entity.ProductImages

fun Product.toDTO(): ProductDTO {
    return ProductDTO(
        productId ?: throw DataNotFoundException("Product ID is missing"),
        productName,
        productPrice,
        productStatus,
        submitDate ?: throw DataNotFoundException("Submit Date is missing")
    )
}

fun Product.toResponse(): ProductResponse {
    val seller = seller
        ?: throw DataNotFoundException("Product ID is missing")
    val category = category
        ?: throw DataNotFoundException("Category is missing")

    return ProductResponse(
        productId ?: throw DataNotFoundException("Product ID is missing"),
        productName,
        productDescription,
        seller.sellerName,
        seller.isA11yGuarantee,
        productPrice,
        productImages.map { it.toResponse() },
        category.parentCategory?.categoryId
            ?: throw DataNotFoundException("Parent Category ID is missing"),
        category.categoryId ?: throw DataNotFoundException("Category ID is missing"),
        category.categoryName
    )
}


fun Product.toDetailResponse(images: List<ProductImages>?, summary: ProductAiSummary?): ProductDetailResponse {
    val seller = this.seller
        ?: throw DataNotFoundException("Seller is missing")
    val category = this.category
        ?: throw DataNotFoundException("Category is missing")

    return ProductDetailResponse(
        productId ?: throw DataNotFoundException("Product ID is missing"),
        productName,
        seller.sellerId ?: throw DataNotFoundException("Seller ID is missing"),
        seller.sellerName,
        seller.sellerGrade,
        seller.isA11yGuarantee,
        productPrice,
        productStatus,
        productDescription,
        productStock,
        images?.map { it.toResponse() },
        category.categoryId ?: throw DataNotFoundException("CategoryId is missing"),
        category.categoryName,
        summary?.summaryText,
        summary?.usageContext,
        submitDate ?: throw DataNotFoundException("Submit Date is missing"),
        summary?.usageMethod
    )
}

fun Product.toCartItemDTO(quantity: Int): CartItemDto {
    val seller = this.seller ?: throw DataNotFoundException("Seller is missing")

    return CartItemDto(
        null,
        null,
        this.productId ?: throw DataNotFoundException("Product ID is missing"),
        seller.sellerId ?: throw DataNotFoundException("Seller ID is missing"),
        seller.sellerName,
        this.productName,
        this.productPrice,
        this.category?.categoryName ?: throw DataNotFoundException("Category is missing"),
        quantity,
        if (this.productImages.isEmpty()) {
            null
        } else {
            this.productImages.first().imageUrl
        }
    )
}

fun Product.toInquireResponse() = ProductInquireResponse(
    productId,
    productName,
    productPrice,
    productStock,
    productStatus,
    category?.categoryName,
    approvedDate,
    updatedAt
)

fun Product.toAdminInquireResponse() =
    ProductAdminInquireResponse(
        productId ?: throw DataNotFoundException("Product ID is missing"),
        productName,
        seller?.sellerId,
        seller?.sellerName,
        seller?.sellerGrade,
        seller?.isA11yGuarantee ?: false,
        productPrice,
        productStatus,
        productDescription,
        productStock,
        category?.categoryId,
        category?.categoryName,
        submitDate ?: throw DataNotFoundException("Submit Date is missing")
    )

fun ProductImages.toResponse() = ProductImageResponse(
    imageId ?: throw DataNotFoundException("Product ID is missing"),
    imageUrl ?: throw DataNotFoundException("Product URL is missing"),
    altText ?: throw DataNotFoundException("Product Alt Text is missing"),
    imageSequence
)