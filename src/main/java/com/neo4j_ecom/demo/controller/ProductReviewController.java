package com.neo4j_ecom.demo.controller;


import com.neo4j_ecom.demo.exception.AppException;
import com.neo4j_ecom.demo.model.dto.request.ProductReviewRequest;
import com.neo4j_ecom.demo.model.dto.response.ApiResponse;
import com.neo4j_ecom.demo.model.dto.response.pagination.PaginationResponse;
import com.neo4j_ecom.demo.model.dto.response.review.ProductReviewResponse;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.service.ProductReviewService;
import com.neo4j_ecom.demo.utils.enums.ErrorCode;
import com.neo4j_ecom.demo.utils.enums.SuccessCode;
import com.neo4j_ecom.demo.utils.helper.PaginationInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/products")
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping("/{productId}/reviews")
    @PreAuthorize("hasRole('USER')")

    public ResponseEntity<ApiResponse<ProductReview>> createProductReview(
            @PathVariable String productId,
            @Valid
            @RequestBody ProductReviewRequest request
    ) {

        log.info("create product review request : productId {}, request {}", productId, request);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.CREATED,
                        productReviewService.createReview(productId, request))
        );
    }

    @GetMapping("{productId}/reviews")
    public ResponseEntity<ApiResponse<PaginationResponse>> getAllReviewsByProductId(
            @PathVariable String productId,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size,
            @RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder

    ) {

        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));

        log.info("get all reviews by product request : {}, page {}, size {}", productId, page, size, sortBy);

        PaginationResponse response = productReviewService.getAllReviewsByProductId(productId, Integer.parseInt(page), Integer.parseInt(size), sortBy, sortOrder);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        response
                )
        );
    }

    @GetMapping("{productId}/reviews/filter")
    public ResponseEntity<ApiResponse<PaginationResponse>> getAllReviewsByVariantIdFilter(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "20") String size,
            @PathVariable String productId,
            @RequestParam(defaultValue = "5") String rating
    ) {
        log.info("get all reviews by product request : {}, rating {}", productId, rating);

        PaginationInput.validatePaginationInput(Integer.parseInt(page), Integer.parseInt(size));

        if (Integer.parseInt(rating) < 1 || Integer.parseInt(rating) > 5) {
            throw new AppException(ErrorCode.WRONG_INPUT);
        }

        PaginationResponse response = productReviewService.getAllReviewsByProductIdFilter(productId, Integer.parseInt(rating), Integer.parseInt(page), Integer.parseInt(size));

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.FETCHED,
                        response
                )
        );
    }

    @PutMapping("/{productId}/reviews/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<ProductReview>> updateProductReview(
            @PathVariable String productId,
            @PathVariable String reviewId,
            @Valid @RequestBody ProductReviewRequest request
    ) {
        log.info("update product review request : productId {}, reviewId {}, request {}", productId, reviewId, request);

        return ResponseEntity.ok(
                ApiResponse.builderResponse(
                        SuccessCode.UPDATED,
                        productReviewService.updateReview(productId, reviewId, request)
                )
        );
    }
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> deleteProductReview(
            @PathVariable String productId,
            @PathVariable String reviewId
    ) {
        log.info("delete product review request: productId {}, reviewId {}", productId, reviewId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builderResponse(
                        SuccessCode.DELETED,
                        productReviewService.deleteReview(productId, reviewId))
        );
    }
}
