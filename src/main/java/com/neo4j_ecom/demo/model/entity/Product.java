package com.neo4j_ecom.demo.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neo4j_ecom.demo.model.dto.request.ProductRequest;
import com.neo4j_ecom.demo.model.entity.ProductVariant.ProductVariant;
import com.neo4j_ecom.demo.model.entity.ProductVariant.VariantOption;
import com.neo4j_ecom.demo.model.entity.Review.ProductReview;
import com.neo4j_ecom.demo.model.entity.Review.ReviewOption;
import com.neo4j_ecom.demo.model.entity.Specfication.ProductSpecification;
import com.neo4j_ecom.demo.model.entity.Specfication.SpecificationOption;
import com.neo4j_ecom.demo.utils.enums.ProductType;
import com.neo4j_ecom.demo.utils.enums.ReviewType;
import com.neo4j_ecom.demo.utils.enums.SellingType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document("products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseEntity {

    @Id
    private String id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private BigDecimal sellingPrice;
    private String description;
    private Float avgRating;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long quantityAvailable;
    private SellingType sellingType;
    private String sku;
    private List<String> productImages = new ArrayList<>();
    private String primaryImage;
    private long soldQuantity;
    private ProductDimension productDimension;
    private List<ProductBanner> productBanners = new ArrayList<>();
    @DocumentReference(lazy = true)
    @JsonIgnoreProperties("products")
    private Brand brand;
    @DocumentReference(lazy = true)
    @JsonIgnoreProperties({"parent", "children"})
    private List<Category> categories = new ArrayList<>();
    @DocumentReference(lazy = true)
    @JsonIgnoreProperties("products")
    private List<ProductReview> reviews = new ArrayList<>();
    private ProductType primaryVariantType;
    @DocumentReference(lazy = true)
    private List<ProductVariant> productVariants;
    private List<SpecificationOption> productSpecifications;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int countOfReviews;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long sumSoldQuantity;

    @Transient
    private Boolean hasVariants;
    @Transient
    private Boolean hasSpecification;
    @Transient
    private Boolean hasCollection;
    @Transient
    private Boolean hasReview;

    public Boolean getHasVariants() {
        return productVariants != null && !productVariants.isEmpty();
    }

    public Boolean getHasSpecification() {
        return productSpecifications != null && !productSpecifications.isEmpty();
    }

    public Boolean getHasDimensions() {
        return productDimension != null;
    }

    public Boolean getHasReview() {
        return reviews != null && !reviews.isEmpty() && countOfReviews > 0;
    }

    //just get id and name of brand
    public Brand getBrand() {
        return Brand.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }

    public List<Category> getCategories() {
        return categories.stream()
                .map(category -> Category.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .isFeatured(category.getIsFeatured())
                        .build())
                .collect(Collectors.toList());
    }


    public void updateProduct(ProductRequest request) {
        this.name = request.getName() != null ? request.getName() : this.name;
        this.description = request.getDescription() != null ? request.getDescription() : this.description;
        this.originalPrice = request.getOriginalPrice() != null ? request.getOriginalPrice() : this.originalPrice;
        this.discountedPrice = request.getDiscountedPrice() != null ? request.getDiscountedPrice() : this.discountedPrice;
        this.sellingPrice = request.getSellingPrice() != null ? request.getSellingPrice() : this.sellingPrice;
        this.productImages = request.getProductImages() != null ? request.getProductImages() : this.productImages;
        this.primaryImage = request.getPrimaryImage() != null ? request.getPrimaryImage() : this.primaryImage;
        this.productDimension = request.getProductDimension() != null ? request.getProductDimension() : this.productDimension;
        this.sellingType = request.getSellingType() != null ? request.getSellingType() : this.sellingType;
        this.quantityAvailable = request.getQuantityAvailable() != null ? request.getQuantityAvailable() : this.quantityAvailable;
        this.sku = request.getSku() != null ? request.getSku() : this.sku;
        this.soldQuantity = request.getSoldQuantity() != null ? request.getSoldQuantity() : this.soldQuantity;
        this.primaryVariantType = request.getPrimaryVariantType() != null ? request.getPrimaryVariantType() : this.primaryVariantType;
        this.productSpecifications = request.getSpecifications() != null && request.getHasSpecification() ? request.getSpecifications() : this.productSpecifications;
    }
}