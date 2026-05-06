package com.multicampus.gamesungcoding.a11ymarketserver.feature.product.service

import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.dto.CategoryResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.mapper.toResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    @Transactional(readOnly = true)
    fun getAllCategories(): List<CategoryResponse> {
        val categories = categoryRepository.findAll()

        val categoryMap = categories.map { it.toResponse() }
            .associateBy { it.categoryId }

        val roots = ArrayList<CategoryResponse>()

        categories.forEach { category ->
            val currentDto = categoryMap[category.categoryId] ?: return@forEach

            val parentId = category.parentCategory?.categoryId

            if (parentId == null) {
                roots.add(currentDto)
            } else {
                categoryMap[parentId]?.addSubCategory(currentDto)
            }
        }

        return roots
    }
}
