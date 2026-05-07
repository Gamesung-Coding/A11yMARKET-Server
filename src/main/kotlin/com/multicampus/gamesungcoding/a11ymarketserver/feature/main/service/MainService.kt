package com.multicampus.gamesungcoding.a11ymarketserver.feature.main.service

import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CatProductInfo
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.CategoryRecommendResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.dto.EventResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.entity.MonthlyPopularProduct
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.mapper.toRecommendResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.mapper.toResponse
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.MainPageEventsRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.MonthlyPopularProductRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.main.repository.RecommendationRepository
import com.multicampus.gamesungcoding.a11ymarketserver.feature.product.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class MainService(
    private val monthlyPopularProductRepository: MonthlyPopularProductRepository,
    private val recommendationRepository: RecommendationRepository,
    private val categoryRepository: CategoryRepository,
    private val mainPageEventsRepository: MainPageEventsRepository,
) {

    fun findTop10ByOrderByRankingAsc(): List<MonthlyPopularProduct> =
        monthlyPopularProductRepository.findTop10ByOrderByRankingAsc()


    fun getAllCategories(): List<CategoryRecommendResponse> {
        val list = recommendationRepository.findAll()

        val roots = categoryRepository.findAllByParentCategoryIsNull()
            .map { it.toRecommendResponse() }
            .associateBy { it.categoryId }

        list.forEach { item ->
            val currentDto = roots[item.rootCategoryId]
            if (currentDto != null) {
                val productInfo = CatProductInfo(
                    productId = item.productId!!,
                    productName = item.productName,
                    productPrice = item.productPrice,
                    productImageUrl = item.productImageUrl
                )
                currentDto.addProduct(productInfo)
            }
        }

        return roots.values.toList()
    }

    fun getAllEvents(): List<EventResponse> {
        val list =
            mainPageEventsRepository.findAllByStartDateBeforeAndEndDateAfterOrderByEventIdAsc(
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        return list.map { it.toResponse() }
    }
}
